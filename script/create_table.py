import sys
import statistics
import numpy as np
import scipy.stats as st

fpath = '../out/Times2.out' # file with times

with open(fpath) as f:
	lines = f.readlines()   # read the content

tests = {}

for line in lines:
	spl = line.split(':')
	if spl[0] not in tests:
		tests[spl[0]] = list()
	tests[spl[0]].append(float(spl[1])) # append measurement under test name key

for key, value in tests.items():
	# print it!
	print(key, ":", len(value), np.mean(value), statistics.stdev(value), st.t.interval(0.95, len(value)-1, loc=np.mean(value), scale=st.sem(value)))