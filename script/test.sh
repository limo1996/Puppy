test_folder="../test/input_prop/"
if [! -z "$1"] then 
    test_folder = "$1"
fi

## Compile source code
if javac -cp soot.jar -d class src/*.java; then 
    echo "[*] Compilation of src OK"
    ## run all test cases
    for file in ${test_folder}*.java; do
        classfile=$(basename $file)
        classname=$(echo $classfile | sed -e 's/\..*$//')
        ## compile current file
        javac -d test $file;
        mkdir -p ../out;
        touch ../out/out.txt;
        ## run it
        if java -cp soot.jar:class research.analysis.RunDataFlowAnalysis $classname; then
            d=$(diff out/out.txt ${test_folder}out/$classname.txt)
            #echo $d
            if [[ -z "${d// }" ]] ; then
                echo "[OK] $file";
            else
                echo "[!] $file output does not match";
                #echo $d
            fi;
        else
            echo "[!] $file Runtime Error";
        fi;
    done
else
    echo "[!] Compilation error of src";
fi;

## Compile, and run tests
## Script should work for most Linux/OSX/MacOS systems

## compiling test cases
#javac -d test test/1/Test.java ;

## compile the code. Your code should compile with this command exactly
#mkdir -p class
#if javac -cp soot.jar -d class src/*.java; then
#    echo "[*] Compilation OK";
	## run test cases
#	if java -cp soot.jar:class csc410.hw3.RunDataFlowAnalysis Test > /dev/null; then
#		echo "[>] Your Upward Exposed Uses:"; cat exposed-uses.txt; 
#		echo "[*] Expected Upward Exposed Uses:"; cat test/1/expected-uses.txt;
#	else
#		echo "[!] Runtime Error";
#	fi;
#else
#	echo "[!] Compilation Error";
#fi;