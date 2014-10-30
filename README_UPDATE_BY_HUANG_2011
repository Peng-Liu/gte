=================
  2013-08-30
=================

1. Modified files: lrs/VPolygon.java | lrs/LrsAlgorithm.java
   Add a new field ray to VPolygon, because the extensive form applies lrs to polyhedra instead of polytop.

2. New file: lrs/vertex.java
   New class added for extensive game with a non-redundancy representation.

3. New file: tree/NonRedundantSequenceForm.java
   Algorithm to remove redundancy.

4. Modified file: tree/SequenceForm.java
   Add a new orginPay field to save the payoff matrices A, B.

5. New file: tree/SequenceFormMain.java
   Tester.Including Myerson's example (1986), Wan's example (2011), and Bernhard's example (2013). 
   
==============
  2013-09-07
==============

Tested the "SimpleLargeGame". Test passed. 

1. The result of strategic form extreme equilibria is in the file "StrategicLargeGame.txt".

2. "textMain.java" calculates the sequence form extreme equilibrium using the new algorithm (Huang 2011). It then reads 
   the strategic form extreme equilbria from the file "StrategicLargeGame.txt".
   
3. All strategic form extreme equilbria are converted into sequence form. They are not necessarily extreme equilbria in 
   sequence form though. So for any such sequence form equilibrium that is converted from an strategic form extreme equilibrium, 
   the program use Guassian matrix operations to determine if probVect2 of this equilibrium is a linear combination of
   the probVect2 of all the equilibria computed by the new algorithm (Huang 2011) that have the same probVect1 as this 
   strategic-form-converted equilibrium.
   
 #3 is satisfied for the SimpleLargeGame.  
   
     
