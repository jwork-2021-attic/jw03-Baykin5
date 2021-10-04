# W03

##### 191220124

#### 1.写下对代码工作原理的理解；

main函数的工作过程与W02基本一致，只不过**这里的Sorter并非自己指定，而是由我们自己定义的一个隐写术图类加载器`SteganographyClassLoader`来进行获取。**

**我们首先用SteganographyEncoder类的对象将我们写的排序java文件和一张任意图片进行编码，得到一张新的隐写术图。**

**然后用隐写术图类加载器`SteganographyClassLoader`加载该类，调用`loadClass()`函数获取图片中的类，用`newInstance()`创建一个该类的实体再设置给Geezer即可。**

##### 1.1 编码过程

因为我们需要将类提前编译好，将编译后的类字节码存入图片，因此这里需要引入`JavaCompiler`类，该类可以依据传入的动态路径进行动态编译得到class文件。

然后利用`ImageIO`的`read()`函数读入原图片，再调用`SteganographyEncoder`的`encodeFile()`函数将class文件的字节码编码进图片，返回值即为新图片（这里传入的文件路径就是将原java文件换成class文件）。

然后调用`ImageIO`的`write()`函数写出新图片。

```java
import javax.tools.JavaCompiler;

public static void getSteganography(String classSource, String originImage) throws IOException {
    String className = classSource.substring(0, classSource.indexOf(".")).replace("/", ".");
    SteganographyFactory.compile(classSource);
    BufferedImage image = ImageIO.read(new File(originImage));
    SteganographyEncoder steganographyEncoder = new SteganographyEncoder(image);

    BufferedImage encodedImage = steganographyEncoder.encodeFile(new File(classSource.replace("java", "class")));
    ImageIO.write(encodedImage, "png", new File(className+".png"));

}
```

整个编码过程的关键在于`encodeFile()`函数，我们来到`SteganographyEncoder.java`文件下。

浏览`encodeFile()`函数，该函数将文件的信息分成四个部分：文件名的字节数，文件名，文件内容的字节数，文件内容。

然后创建一个byte数组`finalBytes`，0-3位存储文件名的字节数，4-7位存储文件内容的字节数，然后是文件名以及文件内容。

最后将`finalBytes`传给`encode()`函数进行编码。

`encode`的具体编码过程就不赘述了，我们只需要知道`encode()`函数获取图片的像素信息`pixel`，然后将字节码以某种方式存入`pixels`数组，再用该数组生成新的图片。

##### 1.2 解码过程

在得到带有类信息的隐写术图后，我们来尝试从生成的图片中获取信息。

```java
SteganographyClassLoader loader = new SteganographyClassLoader(
        new URL("file:///./example.QuickSorter.png"));

Class c = loader.loadClass("example.QuickSorter");
```

**根据java的类加载机制，`loadClass()`首先向上层的ClassLoader发送委派请求，然后再从最顶层依次往下查找。**

**BootstrapClassloader，PlatformClassloader，AppClassloader显然都无法找到.class文件，那么这时候就需要调用我们自己定义的类加载器的findClass()函数了**

> **阅读代码的时候我有这样一个问题，自定义类加载器的代码中并没有指定父加载器的相关内容，那么它是怎么知道AppClassloader是父类加载器的呢？**
>
> **上网查询资料得知，对于自定义类加载器来说，默认其父加载器为加载了这个自定义类加载器的类加载器。**
>
> **由于我们自定义的类加载器存放在本地classpath，由AppClassloader加载，所以其父类加载器为AppClassloader**。

`findClass()`函数中，调用`SteganographyEncoder`的`decodeByteArray()`函数按照编码规则进行相应的逆推解码来获取图片中的字节码，然后再用`defineClass()`函数将获取的字节码生成类，返回即可。

```java
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            BufferedImage img = ImageIO.read(url);

            SteganographyEncoder encoder = new SteganographyEncoder(img);
            byte[] bytes = encoder.decodeByteArray();   //获取字节码
            return this.defineClass(name, bytes, 0, bytes.length);  //由字节码生成类

        } catch (IOException e) {
            throw new ClassNotFoundException();
        }

    }
```

这样我们就完成了整个的类加载过程，得到类的信息后，用`newInstance()`生成该类的对象实体。

```java
Sorter sorter = (Sorter) c.newInstance();
```

#### 2.将自己在`W02`中实现的两个排序算法（冒泡排序除外）分别编码进自选图片得到隐写术图，在markdown中给出两个图片的URL；

因为没有web服务器，所以使用了本地文件路径作为url，**（这里使用的是相对路径）**

SelectSorter:`"file:///./example.SelectSorter.png"`

QuickSorter:`"file:///./example.QuickSorter.png"`

#### 3.用你的图片给`W02`中example的老头赋予排序能力，得到排序结果（动画），上传动画到asciinema，在markdown中给出两个动画的链接。

SelectSort:[jwork-W03-SelectSort - asciinema](https://asciinema.org/a/438553)

QuickSort:[jwork-W03-QuickSort - asciinema](https://asciinema.org/a/438554)

#### 4.联系另一位同学，用他的图片给`W02`中example的老头赋予排序能力，在markdown中记录你用的谁的图片，得到结果是否正确。