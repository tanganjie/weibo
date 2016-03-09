package cn.edu.bistu.common.nlp.libsvm.svm;
public class svm_node implements java.io.Serializable
{
	public int index;
	public double value;

	@Override
	public String toString() {
		return "svm_node{" +
				"index=" + index +
				", value=" + value +
				'}';
	}
}
