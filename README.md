# W03

下图中有一些泡泡

![](https://raw.githubusercontent.com/jwork-2021/jw03/main/example/resources/bubble.jpeg)


下图中也有一些泡泡

![](https://raw.githubusercontent.com/jwork-2021/jw03/main/example.BubbleSorter.png)

这两张图你看得出区别么？你应该是看不出来的。但其实两张图并不一样，后者为一张“隐写术图”（[Steganography](https://zh.wikipedia.org/zh/隐写术))。

我将一个实现冒泡排序的BubbleSorter类的字节码编码进了第一张泡泡图片中，得到了第二张图。为了方便起见，图片被放置在`"https://cdn.njuics.cn/example.BubbleSorter.png`这个地方。

然后`W02`中的`Scene.main()`中的代码即可进行改写：

```java
...
    Geezer theGeezer = Geezer.getTheGeezer();

    SteganographyClassLoader loader = new SteganographyClassLoader(
            new URL("https://cdn.njuics.cn/example.BubbleSorter.png"));

    Class c = loader.loadClass("example.BubbleSorter");

    Sorter sorter = (Sorter) c.newInstance();

    theGeezer.setSorter(sorter);
...
```

请尝试运行example（注意`lib`目录下存在一个jar文件，需要被包含在工程的classpath中）仔细阅读example中的代码，理解其含义，撰写一个markdown文件，完成以下任务：

1. 写下对代码工作原理的理解；
2. 将自己在`W02`中实现的两个排序算法（冒泡排序除外）分别编码进自选图片得到隐写术图，在markdown中给出两个图片的URL；
3. 用你的图片给`W02`中example的老头赋予排序能力，得到排序结果（动画），上传动画到asciinema，在markdown中给出两个动画的链接。
4. 联系另一位同学，用他的图片给`W02`中example的老头赋予排序能力，在markdown中记录你用的谁的图片，得到结果是否正确。

