import sys

# first parameter: master file
# second parameter: file to check
assert len(sys.argv) == 3

def replace_columns(str):
    str2 = list(str)
    inside = False
    for i in range(0, len(str2)):
        c = str2[i]
        if c == '[':
            inside=True
        if c == ']':
            inside=False
        if c == ',' and not inside:
            str2[i] = ';'
    return ''.join(str2)

def parse_solution(file):
    """
        Parses file according to hard defined file format and returns dictionary
        which maps method to dictionary which maps variables to definitions. 
        They are again dictionary which maps conditions to values.
    """
    with open(file) as f:
        lines = f.readlines()
    method = ''
    solution = {}
    for line in lines:
        if line.startswith('method:'):
            method = line[7:-1]
            solution[method] = {}
        else:
            assert method != ''
            idx = line.index(':')
            var = line[0:idx]
            if var == '_hopefully_some_impossible_variable_name_6163361':
                continue
            defs = line[idx+1:]
            assert defs[0] == '{', defs
            defs = defs[1:-1]
            defs = replace_columns(defs)
            # print (defs)
            defs_list = defs.split('; ')
            defs_dic = {}
            for def_line in defs_list:
                dl_splt = def_line.split(']=')
                assert len(dl_splt) == 2, def_line
                dl_splt[0] = dl_splt[0][1:]
                defs_dic[dl_splt[0]] = dl_splt[1]
            solution[method][var] = defs_dic
    return solution

master_solution = parse_solution(sys.argv[1])
solution = parse_solution(sys.argv[2])

error = False
for method, content in master_solution.items():
    #print('method:' + method)
    for var, definitions in content.items():
        #print(var + ':{', end='')
        for condition, value in definitions.items():
            #print(condition + '=' + value, end=',')
            try:
                if(value != solution[method][var][condition]):
                    print('Difference in {0}:{1}:{2}! {3} expected but found {4}'.format(method, var, condition, value, solution[method][var][condition]))
                    error = True
            except KeyError:
                    print('Solution does not contain one of the keys: [{0}, {1}, {2}]'.format(method, content, condition))
        #print('}')

if error:
    sys.exit(1)

print('correct')
sys.exit(0)