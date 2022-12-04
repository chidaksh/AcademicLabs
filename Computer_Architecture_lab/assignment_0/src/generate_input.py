import numpy as np

if __name__ == "__main__":
    p = np.linspace(0.00001,0.9,20,endpoint= True)
    w = np.arange(1,200)

    x,y = np.meshgrid(p,w)
    s = np.dstack((x,y))
    s = s.reshape(s.shape[0]*s.shape[1],s.shape[2])
    #print(s.shape)
    np.savetxt("input.txt",s)