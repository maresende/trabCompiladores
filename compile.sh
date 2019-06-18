#!/bin/sh

cd /home/matheus/compilador/

java java_cup.Main -parser Grm -expect 6 < Parse/Tiger.cup > Grm.out 2> Grm.err & pid=$!
wait $pid
rm  Grm.out  & pid2=$! 
wait $pid2
mv sym.java Parse/ & pid3=$!
wait $pid3
mv Grm.java Parse/ & pid4=$! 
wait $pid4
javac Parse/Grm.java & pid5=$! 
wait $pid5
javac Parse/sym.java& pid6=$! 
wait $pid6
java JLex.Main Parse/Tiger.lex & pid7=$! 
wait $pid7
javac Parse/Tiger.lex.java
