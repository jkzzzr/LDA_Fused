### LDA_Fused 此处代码部分内容：实现的为Liang论文中的方法
#### 实现：应用LDA的检索结果多样化方法，以及一些其他数据集操作 </br>简介：Liang论文的方法大体为三部分：得到CombSum排序结果，应用LDA得到相关参数，最后将参数带入PM2方法得到最后的排序结果
#### core.algorithm.lda包：
　　修改后的LDA方法
#### searchDoc:
　　从数据集中提取某篇文档的源码
#### default包：一些简方法，类功能同类名
　　CBSum_To_WordList.java</br>
　　Find_Accord_PageId.java</br>
　　LC1.java</br>
　　PM2.java</br>
　　QuictSorc2.java</br>
　　Step1_1.java</br>
　　Step1_2.java</br>
　　TiQuFile.java</br>
  
### 注：Entry包下代码为原创，其他包下内容为摘录修改代码。
 
