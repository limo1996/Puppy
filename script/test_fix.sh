
## Compile, and run tests
## Script should work for most Linux/OSX/MacOS systems

## compiling test cases
javac -d ../test ../test/1/Test.java ;

## compile the code. Your code should compile with this command exactly
mkdir -p ../class
if javac -cp soot.jar -d ../class ../src/*.java; then

	## run test cases
	if java -cp soot.jar:../class research.analysis.RunDataFlowAnalysis Test S ../out/Test.out; then
        if python3 comparer.py ../out/Test.mastersol ../out/Test.sol; then
            echo "[OK] ../test/1/Test.java";
        else
            echo "[!] ../test/1/Test.java";
        fi
	else
		echo "[!] Runtime Error";
	fi;
else
	echo "[!] Compilation Error";
fi;