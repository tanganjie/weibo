package cn.edu.bistu.common.socialnet.pagerank;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by tanjie on 11/11/15.
 */
public class UserGraph {
    protected static Logger log = Logger.getLogger(UserGraph.class);
    private int mSize;
    private ArrayList<Integer>[] mAdjacency;
    private double[][] broadCharge;
    private double[] mOutDegrees;

    /**
     * Creates a new graph
     * @param size the number of vertices in the graph
     */
    public UserGraph(int size) {
        mSize = size;
        mAdjacency = new ArrayList[mSize];
        broadCharge = new double[mSize][mSize];

        for(int i = 0; i<broadCharge.length;i++){
            for (int j=0;j<broadCharge[i].length;j++){
                broadCharge[i][j] = 0.0;
            }
        }

        //print(broadCharge);

        for (int i = 0; i < mSize; ++i) {
            mAdjacency[i] = new ArrayList<Integer>();
        }

        mOutDegrees = new double[mSize];
    }

    /**
     * Adds a new directed edge to the graph
     * @param source the source of the edge
     * @param destination the destination of the edge
     */
    public void addEdge(int source, int destination, double value) {
        //log.info(source + " " + destination + " " + value);
        mAdjacency[destination].add(source);
        broadCharge[source][destination] = value;
        mOutDegrees[source] += value;
    }

    /**
     * Computes the Pagerank for the given graph
     * @param beta    the probability to continue the walk, 1 - beta is the probability to randomly jump to another vertex
     * @param epsilon the value to determine when the vector is stationary
     * @return        the Pagerank vector
     */
    public Vector computePagerank(double beta, double epsilon) {
//        print(broadCharge);
//        for(int i=0; i<mOutDegrees.length;i++){
//            System.out.print(mOutDegrees[i] + " ");
//        }
        Vector r = new Vector(mSize, 1.0 / (double)mSize);
        Vector r_ = new Vector(mSize);

        do {
            r_.copyFrom(r);

            for (int i = 0; i < mSize; ++i) {
                r.set(i, 0.0);

                if (mAdjacency[i].size() > 0) {
                    double sum = 0.0;

                    for (Integer j : mAdjacency[i]) {
                        sum += r_.get(j) * broadCharge[j][i] / (double) mOutDegrees[j];
                    }

                    sum *= beta;

                    r.set(i, sum);
                }
            }

            double S = r.sum();

            for (int i = 0; i < mSize; ++i)
                r.set(i, r.get(i) + (1.0 - S) / (double)mSize);

        } while (Vector.absMinus(r, r_).sum() > epsilon);

        return r;
    }

    private void print(double[][] v) {
        System.out.println();
        for(int i=0;i<v.length;i++){
            for (int j=0;j<v[i].length;j++){
                System.out.print(v[i][j]+"\t");
            }
            System.out.println();
        }
    }
}
