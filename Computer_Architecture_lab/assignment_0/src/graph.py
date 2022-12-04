import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D

f=open("input.txt",'r')
prob=[]
width=[]
time=[]
for x in f:
    line=x.split(" ");
    prob.append(float(line[0]))
    width.append(int(float(line[1])))

f=open("output.txt")
for x in f:
    time.append(int(x))
    

prob=prob[:500]
width=width[:500]
fig = plt.figure(figsize=(10,10))
ax = fig.add_subplot(111, projection='3d')
ax.plot(width,prob,time)
ax.set_xlabel("Width")
ax.set_ylabel("Probability")
ax.set_zlabel("\nTime",linespacing=1)
ax.set_title("Time vs Probabilty vs Width")
#ax.view_init(0,0)
plt.show()

# plt.plot(width,prob)
# plt.show()