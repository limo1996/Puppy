import sys
from subprocess import Popen, PIPE, STDOUT
from z3 import *

assert len(sys.argv) == 3
out_file = sys.argv[1]
target_file = sys.argv[2]
line_to_insert = 3

def parse_param(params, model):
	dic = {}
	for p in params:
		s = p.strip().split(':')
		dic[s[0]] = int(s[1])
	res = [0] * len(params)
	for i in model.decls():
		res[dic[i.name()]] = model[i]
	return [str(r) for r in res]

def runZ3(input, params, method):
	global line_to_insert
	global final_lines
	input2 = "".join(input)
	#print(input2)
	smt2 = parse_smt2_string(input2)
	s = Solver()
	s.add(smt2)
	s.check()
	#print(s.model())
	call = "\t\tt.{0}({1});\n".format(method, ",".join(parse_param(params, s.model())))
	#print(call)
	final_lines.insert(line_to_insert, call)
	line_to_insert+=1

with open(out_file, 'r+') as f:
	lines = f.readlines()

with open(target_file, "r+") as f2:
	final_lines = [l for l in f2.readlines() if not l.startswith("\t\tt.")]

smt_lines = []
param_lines = []
method = ""
params = False
all_methods = set()
for line in lines:
	if line.startswith('method:'):
		print(method, len(smt_lines))
		if len(smt_lines) > 0:
			runZ3(smt_lines, param_lines, method)
			param_lines = []
			smt_lines = []
		method = line[7:].strip()
		all_methods.add(method)
	elif line.startswith('('):
		smt_lines.append(line)
	elif line.startswith("Parameters:"):
		params = True
	elif line == "\n":
			params = False
	elif params:
		param_lines.append(line)

with open(target_file, "w") as f3:
	content = "".join(final_lines)
	f3.write(content)

c = Popen(['javac', target_file])
p = Popen(['java', '-cp', target_file.replace("Test.java", ""), "Test"], stdout=PIPE, stderr=STDOUT)
correct_exec = set()
for line in p.stdout:
	l = str(line)
	if l.startswith('b\'Error:'):
		correct_exec.add(l[8:].replace("\\n'", ""))

diff = all_methods - correct_exec
#ignore those
diff.remove('main')
diff.remove('<init>')
diff.remove('error')

if len(diff) == 0:
	print("All OK")
	sys.exit(0)
else:
	print("Wrong: ", diff)
	sys.exit(1)