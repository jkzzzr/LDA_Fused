package cn.edu.processTerm;

/**
 * implement the quicksort algorithm by java,the data type of each element is string
 * ��С��������
 * @author jiangsu
 *
 */

public class QuickSort<T extends Record> {
	T data[];
	public QuickSort(T data[]){
		this.data=data;
	}
	
	private int partition(T sortArray[], int low, int hight) {
		T key = sortArray[low];
		while (low < hight) {
			while (low < hight && sortArray[hight].compareTo(key)>=0)
				hight--;
			sortArray[low] = sortArray[hight];
			while (low < hight && sortArray[low].compareTo(key)<=0)
				low++;
			sortArray[hight] = sortArray[low];
		}
		sortArray[low] = key;
		return low;
	}
    /**
     * ����keyֵ����
     * @param low
     * @param hight
     * @return
     */
	public T[] sort(int low, int hight) {
		if (low < hight) {
			int result = partition(data, low, hight);
			sort(low, result - 1);
			sort(result + 1, hight);
		}
		return data;
	}
}
