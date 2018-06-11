---
layout:     post
title:      "Effective C++"
subtitle:   "Effective C++"
date:       2018-06-07 15:00:00
author:     "Xu"
header-img: "img/post-bg-2015.jpg"
catalog: true
tags:
    - Effective C++
---

# Effective C++

* [1.让自己习惯C++](#1.让自己习惯C++)
    - [条款1:视C++为一个语言联邦](#条款1视C++为一个语言联邦)
    - [条款2:尽量以const,enum,inline替换#define](#条款2尽量以const,enum,inline替换#define)
    - [条款3:尽可能使用const](#条款3:尽可能使用const)
    - [条款4:确定对象被使用前已被初始化](#条款4确定对象被使用前已被初始化)
* [2.构造/析构/赋值运算](#2构造/析构/赋值运算)
    - [条款05：了解C++默默编写并调用哪些函数](#条款05了解C++默默编写并调用哪些函数)
    - [条款06:若不想使用编译器自动生成的函数，就该明确拒绝](#条款06:若不想使用编译器自动生成的函数，就该明确拒绝)
    - [条款07:为多态基类声明virtual析构函数](#条款07为多态基类声明virtual析构函数)
    - [条款08:别让异常逃离析构函数](#条款08别让异常逃离析构函数)
    - [条款09:绝不在构造和析构函数中调用virtual函数](#条款09绝不在构造和析构函数中调用virtual函数)
    - [条款10:令operator=返回一个reference to \*this](#条款10令operator=返回一个reference-to-this)
    - [条款11:在operator=中处理自我赋值](#条款11在operator=中处理自我赋值)
    - [条款12:复制对象时勿忘其每一个成分](#条款12复制对象时勿忘其每一个成分)
* [3.资源管理](#3.资源管理)
    - [条款13:以对象管理资源(智能指针对象)](#条款13以对象管理资源(智能指针对象))
    - [条款14:在资源管理类中注意copying的行为](#条款14在资源管理类中注意copying的行为)
    - [条款15:在资源管理类中提供对原始资源的的访问](#条款15在资源管理类中提供对原始资源的的访问)
    - [条款16:成对使用new和delete时要采取相同的形式](#条款16成对使用new和delete时要采取相同的形式)
    - [条款17:以独立语句将newed对象置入智能指针](#条款17以独立语句将newed对象置入智能指针)

* [4.设计与声明](#4.设计与声明)
    - [条款18:让接口容易被正确使用，不易被误用](#条款18让接口容易被正确使用，不易被误用)
    - [条款19:设计class犹如设计type](#条款19设计class犹如设计type)
    - [条款20:宁以pass-by-reference-to-const替换pass-by-value](#条款20宁以pass-by-reference-to-const替换pass-by-value)
    - [条款21:必须返回对象时别妄想返回其reference](#条款21必须返回对象时别妄想返回其reference)
    - [条款22:将成员变量设置为private](#条款22将成员变量设置为private)
    - [条款23:宁以non-member、non-friend替换member函数](#条款23宁以non-membernon-friend替换member函数)
    - [条款24:若所有参数皆需类型转换，请为此采用non-member函数](#条款24若所有参数皆需类型转换请为此采用non-member函数)
    - [条款25:考虑写出一个不抛异常的swap函数](#条款25考虑写出一个不抛异常的swap函数)
* [5.实现](#5.实现)
    - [条款26:尽可能延后变量定义式出现的时间](#条款26尽可能延后变量定义式出现的时间)
    - [条款27:尽量少做转型动作](#条款27尽量少做转型动作)
    - [条款28:避免返回handles指向对象内部成分](#条款28避免返回handles指向对象内部成分)
    - [条款29:为“异常安全”而努力是值得的](#条款29为异常安全而努力是值得的)
    - [条款30:透彻了解inlining的里里外外](#条款30透彻了解inlining的里里外外)
    - [条款31:将文件间的编译依存关系降到最低](#条款31将文件间的编译依存关系降到最低)
* [6.继承与面向对象设计](#6继承与面向对象设计)
    - [条款32: 确定你的public继承塑膜出is-a关系](#条款32确定你的public继承塑膜出is-a关系)
    - [条款33:避免遮掩继承而来的名称](#条款33避免遮掩继承而来的名称)
    - [条款34:区分接口继承和实现继承](#条款34区分接口继承和实现继承)
    - [条款35:考虑virtual函数以外的其他选择](#条款35考虑virtual函数以外的其他选择)
    - [条款36:绝不重新定义继承而来的non-virtual函数](#条款36绝不重新定义继承而来的non-virtual函数)
    - [条款37:绝不重新定义继承而来的缺省参数值](#条款37绝不重新定义继承而来的缺省参数值)
    - [条款38:通过复合塑膜出has-a或“根据某物实现出”](#条款38通过复合塑膜出has-a或“根据某物实现出”)
    - [条款39:明智而审慎地使用private继承](#条款39明智而审慎地使用private继承)
    - [条款40:明智而审慎地使用多重继承](#条款40明智而审慎地使用多重继承)
* [7.模型与泛型编程](#7模型与泛型编程)
    - [条款41:了解隐式接口和编译期多态](#条款41了解隐式接口和编译期多态)
    - [条款42:了解typename的双重意义](#条款42了解typename的双重意义)
    - [条款43:学习处理模版化基类内的名称](#条款43学习处理模版化基类内的名称)
    - [条款44:将与参数无关的代码抽离templates](#条款44将与参数无关的代码抽离templates)
    - [条款45:运用成员函数模版接受所有兼容类型](#条款45运用成员函数模版接受所有兼容类型)
    - [条款46:需要类型转换时请为模版定义非成员函数](#条款46需要类型转换时请为模版定义非成员函数)
    - [条款47：请使用traits classed表现类型信息](#条款47请使用traits classed表现类型信息)
    - [条款48:认识template元编程](#条款48认识template元编程)
* [8.定制new与delete](#8定制new与delete)
    - [条款49：了解new-handler的行为](#条款49了解new-handler的行为)
    - [条款50：了解new和delete的合理替换时机](#条款50了解new和delete的合理替换时机)
    - [条款51：编写new和delete时需固守常规](#条款51编写new和delete时需固守常规)
    - [条款52：写了placement new也要写placement delete](#条款52写了placement new也要写placement-delete)


## 1.让自己习惯C++

### 条款1:视C++为一个语言联邦

我们要知道C++不只是所有的语言遵循一个规范，C++是多套语言机制融合起来形成的语言联邦，它可以接受多套编程模式，更灵活，功能更强大：

1. C语言：C++完全融合C语言，以C作为基础，C++实际上就是C 的高级解法
2. Object-Oriented C++：面向对象的C＋＋，结合面向对象的思想，编译器实现面向对象中的classs,封装，继承，多态，虚函数等等
3. Template C++:泛型编程
4. STL：是在模版的基础上，封装出一系列标准数据结构容器，算法，迭代器，函数对象供编程人员更方便的使用

C++是一个四个次语言组成的联邦政府，每个次语言都有自己的规约。

### 条款2:尽量以const,enum,inline替换#define

"宁可多让编译器多做一点工作，不让预处理器做过多工作"
#### const替换#define
对于#define 在预处理阶段直接进行替换：#define ASPECT_RATIO 1.653 

* 在编译器开始编译阶段，ASPECT_RATIO就已经被预处理器移走了，该名称并**没有进入记号表**内。
* 当你使用该常量导致错误时，并不知道是ASPECT_RATIO引发的错误，导致**错误追踪困难**
* 还有可能导致目标码1.653**重复出现**多份

解决方法：用const替换#define

const doublei AspectRatio = 1.653;

#### enum
* 如果有的编译器不允许"static整型class常量"在类内完成**初值设定**，可以改用"the enum hack"的替代做法。
* “一个属于枚举类型的数值可以权充ints被使用”
* enum行为某方面更像#define而不像const
    - 和#define一样，取enum的地址就不合法
    - 实用主义，模版元编程的基础

#### inline替换宏

我们知道用#define来定义宏会有很多麻烦，但是这种方式效率很高，为了实现这种效率又要保证函数的可预料行为及类型安全检查，我们可以用内联函数inline来替换宏

#### 总结

1. 对于单纯常量，最好用const对象或enums替换#defines
2. 对于形似函数的宏，最好改用inline函数替换#defines

### 条款3:尽可能使用const

const最具有威力的用法是面对函数声明时的应用，在一个函数声明式内，const可以和函数返回值、各参数、函数自身产生关联

* 令函数返回一个const常量值,可以降低客户错误而造成的意外

```
class Rational{...};
const Rational operator*(const Rational &lhs,const Rational& rhs);

Rational a,b,c;

(a*b) = c;//在a*b的返回值上进行赋值操作。可能是因为单纯的打字错误(a*b) == c;
```

#### const 成员函数

使用const作用于成员函数的两个目的：

1. 便于理解
2. 可以操作const对象

并且两个成语函数可以根据参数是否具有**const特性而进行重载**

一个成员函数是const目前有两个流行的概念：

1. **bitwise constess**:该const成员函数**不允许有**任何对象进行**修改**
2. **logical constess**:该const成员函数**可以修改**它所处理的non-static对象内的某些bits.

**编译器**往往采用的是**bitwise constess**的概念，但现实编程过程往往需要实现logical const的概念，所以我们使用**mutable来释放掉non-static成员变量的bitwise constess约束**

#### const和non-const成员函数中避免重复

因为往往一个函数可能针对是否是const有**两个重载版本**，但这两个重载版本中可能会有重复的步骤：边界检验，日志数据访问，检验数据完整性等等。。为了避免这些**重复代码的生成，并影响编译的效率**。我们往往采用如下做法：

* non-const调用用const版本的函数，const版本实现这些必要的相同步骤，掉用过程中会做**两次相应的转换操作**
    - non_const调用const传入的参数**添加const属性，使用static_cast**<const >
    - const函数返回值**去掉const属性 const_cast**<T&>;
* 为什么不是const调用non_const?
    - 因为non_const不能保证参数是否被修改，从而影响了const成员函数对对象不做改动的承诺

#### 总结

1. 将const作用于任何作用域内的对象、函数参数、函数返回类型、成员函数本体可以检测到某些错误的用法
2. 编译器强行实施bitwise ，但编写程序时应该使用“概念上的常量性”
3. 当const和non-const成员函数有着实质等价的实现时，令non-const版本调用const版本可以避免重复

### 条款4:确定对象被使用前已被初始化

读取未初始化的值会导致不明确的行为，在某些平台上，仅仅是读取未初始化的值就可能让你的程序终止运行。更可能的情况是读入一些"半随机"的bits,污染正在读取的那个对象，导致不可测知的程序行为

按对象类型进行划分：

* 内置类型：永远在使用对象之前进行初始化
* 类类型：初始化责任在于构造函数，确保每一饿构造函数都将对象的每一个成员初始化
    - 对象的成员变量初始化发生在进入构造函数本体之前
    - 先调用**构造函数**再调用**拷贝赋值函数**的效率比只调用一次**拷贝构造函数**的效率低
    - 成员变量初始化的顺序和声明顺序一致

不同编译单元内定义之non-local static对象的初始化顺序，现在我们关心一个问题：

* 当有两个源码文件是，每一个源码文件至少一个non-local static对象，真正的问题是：
    - 如果一个源码文件内的某个non-local static对象的初始化动作**使用**了另一个原文件中的non-local static对象。
    - 但是C++对于“定义于不同编译单元内的non-local static对象”的**初始化顺序并没有明确定义**，所以初始化过程就会出现问题

{% highlight c++ %}
//源文件1
class FileSystem{
    public:
    ...
    std::size_t numDisk() const;
    ...
};
extern FileSystem tfs;//non-local static对象

//源文件2
class Directory{
    public:
        Directory(params);
        ...
};

Directory::Directory(params){
    ...
    std::size_t disks = tfs.numDisks();//使用源文件1中的non-local static对象tfs
    ...
}
{% endhighlight %}


* 解决方案：设计模式中的单例模式
    - 将该non-local static对象设置为local static，即放入到一个**函数**中去，该函数返回该**对象的引用**
    - 另一个源文件调用该对象时，要通过**调用该函数得到该对象的引用**。
    - 这是因为：C++保证，函数内的 **local static**对象会在 **"该函数被调用期间"**和 **“首次遇到该对象之定义式”**时被初始化

{% highlight c++ %}

/源文件1
class FileSystem{
    public:
    ...
    std::size_t numDisk() const;
    ...
};
//将该non-local对象放到函数内部
FileSystem& tfs(){
    static FileSystem fs;//local static对象
    return fs;
}

//源文件2
class Directory{
    public:
        Directory(params);
        ...
};

Directory::Directory(params){
    ...
    std::size_t disks = tfs().numDisks();//使用源文件1中的全局函数tfs获取local static变量fs
    ...
}
{% endhighlight %}

这种结构下的reference-returning函数非常简单，往往第一行定义一个local static变量，第二行返回该对象。这样的函数的简单的性质非常使用成为inline函数

### 总结

1. 为内置对象进行手工初始化，因为c++不保证初始化他们
2. 构造函数最好使用成员初始化列表，不要在构造函数本体内部使用赋值函数
3. 为“免除跨编译单元之初始化次序”问题，请以local static对象替换non-local static对象


## 2.构造/析构/赋值运算

### 条款05：了解C++默默编写并调用哪些函数

* C++的一个对象会在你没有声明拷贝控制成员（构造，拷贝构造，拷贝赋值，析构，移动）时，会自动合成这些成员并且仅在这些函数被需要（被调用，它们才会被编译器创建出来）

* 当一个类对象中含有**引用&或const成员**时，编译器将会**拒绝编译**拷贝赋值函数，因为引用不能改指不同的对象，且const成员不能被修改

* 如果某个基类将拷贝赋值操作符声明为private,编译器将会拒绝为其派生类合成拷贝赋值函数，因为派生类的拷贝赋值函数需要调用基类的拷贝赋值函数

### 条款06:若不想使用编译器自动生成的函数，就该明确拒绝

有些类代表的对象天生就是独一无二的，拷贝显得不合常理，为了阻止编译器自动合成这些拷贝赋值函数有如下几种做法：

1. 将对应的**成员函数设置为private**,外界不能调用拷贝控制成员，但是内部成员函数还是可以调用
2. 使用Uncopyable这样的基类，**阻止**编译器为其**派生类**合成拷贝控制成员
3. 使用delete明确删除？

```c++

class Uncopyable{
    protected:
        Uncopyable(){}
        ~Uncopyable(){}
    private:
        Uncopyable(const Uncopyable&);//将拷贝函数设置为private,派生类无法自动合成
        Uncopyable& operator=(const Uncopyable&);
}；

```

### 条款07:为多态基类声明virtual析构函数

当我们实现**多态**时，往往用一个**基类指针指向派生类**，然后调用该指针指向对象的析构函数时，如果该基类的析构函数不是一个虚函数，则析构部分只会析构基类的部分，派生类的数据并没有被析构，这就导致**“局部销毁”**，形成资源泄漏。

解决思路：
* 为每一个实现**多态用途**的类声明一个虚析构函数，如果class带有任何虚函数，它就应该拥有一个虚析构函数
* Class的设计目的如果不是为了作为**基类**使用，或不是为了具备**多态性**，就不该声明虚析构函数

### 条款08:别让异常逃离析构函数

为什么析构函数中不能抛出异常？

解答：

* 因为我们的**异常处理流程是栈展开**的模式来处理异常
* 在函数栈展开的过程中要**销毁局部对象**
* 当这些局部对象被析构的过程当中，若再次抛出异常，使得程序中有**两个异常同时存在**，此时程序若不是**结束执行**，就是导致**不明确的行为**

但是我们的析构函数部分需要执行可能抛出异常的步骤时怎么办，如关闭数据库连接：

解决办法：

1. 设计一个**接口**让客户自行处理可能抛出异常的操作，析构函数部分只需要检测步骤是否成功执行，若没有再执行该步骤（关闭数据库连接）
2. 当析构函数部分发生异常时，阻止该异常抛出
    - try catch获取异常后，调用abort()强迫程序结束，抢先阻止"不明确行为的发生"
    - try catch获取异常后，不做处理，吞掉该异常，防止该异常抛出

#### 总结
1. 析构函数不要抛出异常，如果发生异常，应该**结束程序或直接吞掉异常**
2. 如果客户需要多某个操作函数运行期间的异常做出反应，class应该设计**对应的接口**执行该操作（而不是在析构函数中）

### 条款09:绝不在构造和析构函数中调用virtual函数

#### 构造函数
在构造函数中调用虚函数：

* 假设有一个class继承体系，基类的构造函数中调用虚函数
* 当派生类开始构造时，首先调用的是基类的构造函数
* 此时**虚函数表指针**还是指向基类的虚函数表，调用的虚函数也是基类版本的虚函数,显然不是我们想要的。（因为既然是虚函数，我们自然希望调用的时候调用的是各派生类不同的版本）
* 基类的构造期间virtual函数绝不会下降到derived classed阶层

为什么要这么设计？

1. 因为在基类的构造期间，派生类中的**成员变量尚未初始化**，若调用派生类版的虚函数绝对要使用派生类中未初始化的成员，这会导致**不明确行为**的发生
2. 派生类对象在基类的构造期间，对象的类型是base class而不是derived class,不只是虚函数调用的是基类的版本，使用**运行期类型信息**typeid或dynamic_cast都会视为基类类型

#### 析构函数

和构造函数的原理类似，因为调用派生类的析构函数，先**释放派生类中的成员变量**，然后调用基类的析构函数，此时对象已经成为一个**基类对象**,调用的虚函数也是基类版本的虚函数。

且如果该虚函数在基类中是一个**纯虚函数**，基类的构造函数和析构函数调用这个虚函数会报出**连接错误**，这个虚函数**必须有定义**。

#### 解决办法：

将该虚函数改为**非虚函数**，然后调用派生类的构造函数或析构函数的时候传递参数（相关信息）到该非虚函数中去。

#### 总结

1. 在base class构造和析构期间调用的virtual函数**不可下降至**derived class
2. 在析构和构造期间**不要调用virtual**函数，因为这类调用**不会下降**到derived class

### 条款10:令operator=返回一个reference to *this

为了实现“连锁赋值”，赋值操作符必须返回一个reference，这个协议不仅适用于标准的赋值形式，也适用于所有赋值相关运算：+=,-=,*=

### 条款11:在operator=中处理自我赋值

```c++
class Bitmap{...};
class Widget{
    ...
    private:
    Bitmap * pb;
};
```

#### 自我赋值可能出现问题的赋值实现：

```c++
Widget&
Widget::operator=(const Widget& rhs){
    delete pb;
    pb = new Bitmap(*rhs.pb);
    return *this;
}

```
分析：当pb和传入进来的rhs.pb指向**同一个对象Bitmap**时,delete会将该Bitmap对象删除掉，导致new Bitmap的过程中pb指向一个被删除的对象。这就会发生错误。

解决办法：添加证同测试

#### 证同测试
```c++
Widget &Widget::operator=(const Widget& rhs){
    if(this == &rhs) return *this;//如果是自我赋值，则直接返回

    delete pb;
    pb = new Bitmap(*rhs.pb);
    return *this;
}
```

异常安全性往往可以自动获得“自我赋值安全”的回报。因为自我赋值的过程中出现错误也会发生异常：

#### 考虑异常安全的实现版本

```c++
Widget &Widget::operator=(const Widget& rhs){
    Bitmap * pOrig = pb;
    pb = new Bitmap(*rhs.pb);
    delete pOrig;
    return *this;

}

```

分析：

* 即使new Bitmap过程中发生异常，本对象的pb依然指向原对象。
* 当发生自我赋值时，先不删除pb所指的对象，而是保留一个指针副本pOrig，然后pb指向一个新的Bitmap后再对本对象进行删除操作

#### 考虑异常安全的copy and swap技术

**by reference版本**
```c++
class Widget{
    ...
    void swap(Widget &rhs);//交换*this和rhs的数据
    ...
}；

Widget& Widget::operator=(const Widget& rhs){
    Widget temp(rhs);//创建一个临时对象
    swap(temp);//将该临时对象和本对象进行交换，局部变量会自动销毁
    return *this;
}

```

**by value版本**

```c++

Widget& Widget::operator=(Widget rhs){
    //不用创建一个临时对象，因为rhs参数本身就是一个拷贝副本
    swap(temp);//将该临时对象和本对象进行交换，局部变量会自动销毁
    return *this;
}
```

#### 总结
1. 保证自我赋值安全和异常安全
2. 利用**语句顺序**或by reference和by value的**swap and copy**技术来保证异常安全和自我赋值安全

### 条款12:复制对象时勿忘其每一个成分

1. 拷贝函数（拷贝构造和拷贝赋值）应该确保复制**“对象内每一个成员变量”**及**“所有base class的成分”**
2. 不要尝试在一个拷贝函数（如拷贝构造）调用另一个拷贝函数（拷贝赋值）。应该将共同机能的**重复代码放入第三个成员函数中**供两个拷贝函数调用。

## 3.资源管理

### 条款13:以对象管理资源(智能指针对象)

资源管理可能出现的问题：当一个工厂函数创建一个我们想要获取的资源对象时，该工厂函数内部会进行**动态内存分配**，所以我们需要手工对该资源进行**释放delete操作**。但可能因为某种异常的发生或提前return函数，导致该资源**没有被回收**造成资源泄漏。

解决的思路：

* 把资源放入对象中进行管理，将释放资源的部分放入析构函数中处理，然后依赖C++的**析构函数**自动调用机制确保资源被释放。（只要函数调用结束，局部变量的对象都会自动调用析构函数）
* 利用智能指针auto_ptr，指向资源对象，当智能指针被销毁时，会调用对象的析构函数进行资源释放

资源管理的两个重要思路：

1. 获得资源后立刻放进**管理对象(智能指针)**内部：工厂函数createInvestment()返回的资源被当作管理对象，智能指针的初值。这种“以对象管理资源”的观念被称为“资源取得时机便是初始化时机（RAII）”
2. 管理对象运用**析构函数确保资源被释放**

智能指针的问题：

* 对于auto_ptr指针指向的对象，不允许**有多个auto_ptr**同时指向一个对象，auto_pte通过拷贝构造函数和拷贝赋值函数复制它们，会进行**管理权的移交，原auto_ptr将会指向空**
    
    ```c++
    std::auto_ptr<Investment> pInv1(createInvestment());
    std::auto_ptr<Investment> pInv2(pInv1);//发生拷贝构造，此时pInv1指向null
    pInv1 = pInv2;//发生拷贝赋值，此时pInv2指向null
    ```
* 为发生正常的复制行为，引入“计数型智慧指针RCSP”。如shared_ptr
* createInvestment()工厂函数返回的“未加工指针”，简直是对资源泄漏的一个明显的漏洞，因为调用者极其容器忘记对这个指针调用delete。我们将在条款18中对这个接口进行修改

### 总结

1. 为防止资源泄漏，请使用**RAII 对象，它们在构造函数中获得资源，并在析构函数中分配资源**
2. 两个常用的**RAII对象**为：管理对象shared_ptr和auto_ptr，其区别主要在于拷贝的过程。

### 条款14:在资源管理类中注意copying的行为

* 并不是所有的资源都是基于堆的（动态内存分配）的资源。这些资源是可以直接使用指针指向它们。所以可以直接使用智能指针进行管理即可,因为智能指针的析构函数**默认调用delete**。
* 有些资源如锁，我们需要**设计一个RAII类**对锁资源进行管理,同样满足RAII规范,在构造过程中获得锁，在析构过程中释放锁(不是通过delete释放的)

```c++
class Lock{
    public:
        explicit Lock(Mutex *pm):mutexPtr(pm){
            lock(mutexPtr);//构造函数中获得锁
        };
        ~Lock(){ 
            unloc(mutexPtr);//析构函数中释放锁
        }
    private:
    Mutex * mutexPtr;
}
```

当一个RAII的对象被复制时会发生什么？

1. **禁止复制**：实现方式见条款6，因为许多RAII对象被复制并不合理
2. 对底层资源使用**“引用计数法”**：实际资源不做拷贝，只递增引用计数，如shared_ptr就是如此。当引用计数为0时删除所指资源。

```c++
    class Lock{
        public:
            explicit Lock(Mutex):mutexPtr(pm,unlock){
                lock(mutexPtr.get());
            }
        private: 
            std::tr1::shared_ptr<Mutex> mutexPtr;//使用shared_ptr
    }

```

这里使用shared_ptr指向锁资源，并传入释放锁的析构函数unlock:

* 所以该类Lock不需要实现析构函数
* 复制mutexPtr时也只是增加锁资源的引用计数而已


* **复制底部资源**：有时候我们需要复制的不是一个指向资源的shared_ptr，只增加其引用计数，而是需要**复制实际的资源**，同时得到一个新的shared_ptr指向该新的资源副本。这属于**深度拷贝**
* **转移底部资源控制权：**同shared_ptr,只不过**底层用auto_ptr**实现，复制时即进行控制权的交接

### 条款15:在资源管理类中提供对原始资源的的访问

**C APIs往往要求访问原始资源（被RAII对象所管理的原始资源），所以每一个RAII class应该提供一个“取得其所管理之资源”的办法**

两个办法：

* 显式转换（不方便，但安全）：为RAII class提供一个get()接口直接返回其管理资源指针
* 隐式转换（方便使用，不安全）：
    - 概念：   
        + 转换构造函数：将其他类型数据隐式转换成本类类型对象
            * 格式：构造函数只有一个参数
        + 隐式转换函数：将本类类型对象转换为指定类型的数据
            * 格式：operator 类型（）｛｝
    - 在对应场景，RAII会隐式转换为原始资源的数据。

### 条款16:成对使用new和delete时要采取相同的形式

new和 delete成对使用，new［］和delete［］成对使用：

* 如果你调用**new**的时候，使用**delete[]**释放内存，由于delete[]寻找数组元素个数的机制为**cookies机制**，配置一块额外的字来存放数大小，再根据该大小来逐个析构元素。但若只有一个元素，delete则有可能**获取到一个未知的数字当作数组大小从而造成不可预知的错误**
* 如果你调用 **new[]**的时候，使用**delete**来释放内存，最后有可能只析构该元素的**第一个元素**，并释放第一个元素的内存而已，其它元素都没有释放

### 条款17:以独立语句将newed对象置入智能指针。

考虑一个对象Widget,我们想要使用智能指针管理这歌对象，并传入到processWidget函数中去，该函数有两个参数，一个就是智能指针，另一个则是优先级。

```c++
processWidget(std::tr1::shared_ptr<Widget> pw,int priority);
//调用该函数
processWidget(std::tr1::shared_ptr<Widget>（new Widget）,priority());
```

C++和java以及C#不同，那两种语言总是以特定次序完成函数参数的计算，C++则不一定，所以C++有三种可能的参数计算顺序：

* 第一种
    - new Widget
    - tr1::shared_ptr构造函数
    - 调用priority()
* 第二种：
    - 调用priority()
    - new Widget
    - tr1::shared_ptr构造函数
* 第三种
    - new Widget
    - 调用priority()
    - tr1::shared_ptr构造函数

对于第三种顺序：

* 当new一个对象时返回一个原生指针
* 然后调用priority（）函数
* 如果发生异常，则原生指针可能丢失，发生内存泄漏。因为此时还没有存入智能指针进行管理

解决办法：使用**单独的语句**将newed的对象放入到智能指针对象管理

```c++
std::tr1::shared_ptr<Widget> pw(new Widget);

processWidget(pw,priority());
```
## 4.设计与声明

### 条款18:让接口容易被正确使用，不易被误用

* 保证接口的**一致性**：如C++ STL容器都提供size()返回容器大小，但是Java和.Net对于不同容器大小接口可能不同，这会增加使用负担
* 保证和内置类型的行为兼容
* 尽量少的**要求用户去记住**执行某种操作，减少使用的过程中出现遗漏等错误：如createInvestment()**返回一个原生指针**，就要求用户去记得delete该指针，这就很容易出现用户没有删除指针，或删除两次相同的指针这些错误
    - 所以正确的接口createInvestment（）的设计应该直接返回智能指针，不需要用户操心指针删除的操作
* **阻止误用**的办法包括：建立新类型，限制类型上的操作，束缚对象值，以及消除客户的资源管理责任

### 条款19:设计class犹如设计type

在设计class时，下列问题将导致class你的设计规范：

* 新type的对象应该如何被创建和销毁？
    - 构造函数、析构函数
    - 内存分配和释放函数：new,new[],delete,delete[]
* 对象的**初始化**和对象的**赋值**该有什么样的差别？
* 新type的对象如果被passed by value，意味着什么？
    - 拷贝构造函数定义passed by value的方式
* 什么是新type的“合法值”？
* 你的新type需要配合某个继承体系吗？
* 你的新type需要什么样的转换？
    - 隐式转换和显示转换的设计
    - 转换构造函数和类型转换函数的设计
* 什么样的操作符和函数对此新type而言是合理的？
    - 成员函数的设计
* 什么样的标准函数应该驳回？
* 谁该取用新type的成员？
* 什么是新type的“未声明接口”？
* 你的新type有多么一般化？
    - 是否应该使用template使得该type更具一般化
* 你真的需要一个新type吗？

### 条款20:宁以pass-by-reference-to-const替换pass-by-value

用pass-by-value进行参数传递有两个弊端：

1. 需要创建临时变量，并调用**拷贝构造函数**，代价昂贵
2. 可能发生**对象切割**问题，当pass-by-value的方式进行参数传递时，如果参数格式是要求一个**基类对象**，而实际的传递过程中传递的是一个**派生类对象**，在调用拷贝构造的过程中，会将该派生类视为基类对象，从而只拷贝基类部分的数据，导致对象切割。使用引用或指针则不会

#### 总结

* 尽量以pass-by-reference-to-const替换pass-by-value,前者通常比较高校，并且可以避免切割问题
* 以上规则并不适用于内置类型以及STL的迭代器和函数对象，对它们而言pass-by-value往往比较适当

### 条款21:必须返回对象时别妄想返回其reference

* 当某些函数的的确确在执行的过程中**创建了新的对象**，该新的对象就是我们函数的执行结果的返回值，如果我们仅仅返回的是该新结果对象的引用时，由于它是**局部变量**，会在函数 **退出时销毁**，这会导致返回的引用指向的是一个已经被析构的对象

思考：

* 假设我们函数内部，在**heap结构**上分配并构造了一个新的结果对象，然后返回该结果对象的引用

```c++
const Rational & operator*(const Rational &lhs,const Rational &rhs){
    Rational * result = new Rational(lhs.n*ths.n,lhs.d*rhs.d);
    return *result;
}
```

* 上述代码，会引起内存泄漏
    - 假设我们执行： 
        
        ```
        Rational w,x,y,z;
        w = x*y*z;
        ```
    - x*y会产生一个新对象，(x*y)*z又会产生一个新的对象，但之前产生的**新的对象的原生指针已经丢失(局部变量自动销毁)**，却没有进行delete从而导致内存泄漏。

#### 总结

* 绝对不要返回pointer 或reference指向一个**local stack**对象，或返回reference指向一个**heap-allocated**对象，或返回一个pointer或reference指向**local static**对象。
* 当需要**返回一个新对象**作为结果时，直接pass-by-value返回该对象即可

### 条款22:将成员变量设置为private

* 将成员变量声明为private:
    - 可以赋予客户**访问数据的一致性**
    - 可细微划分**访问控制**（可读可写）
    - 允许变量的**约束条件**获得保证（不能随意设置变量为任意值）
    - 并使得class设计者有充分的**实现弹性**，因为内部数据对用户是透明的,用户不用了解接口的实现方式。
* protected并不比public更具有封装性

### 条款23:宁以non-member、non-friend替换member函数

设计原则：类中的成员函数只保留那些直接和数据进行交互的基础数据操作部分的函数，那些**间接通过调用其他成员函数**而实现某种特定功能的成员函数(类似于**工具函数**)，完全可以使用非成员(non-member)函数来替换

* 原因：提高类中数据的封装性，当可以访问类中private部分数据的**代码（成员函数和友元函数就可以访问）越少，封装性越高**。
* 一般的实现模式是将这些工具函数用non-member函数的形式实现，然后和该类放在**同一个namespace**下。
    - 因为**namespace 可以跨越多个源码文件**而class不能
    - 意味着客户可以**轻松扩展**这一组和class相关的便利工具函数
* 不一定非是非成员函数，可以是**其它类的成员函数**,比如我们设计一个**工具类**，在该工具类中的成员函数实现对该class的相关操作，一样可以保证这个class的封装性

#### 总结
宁可拿“non-member non-friend”函数替换member函数，这样做可以增加封装性、包裹弹性和机能扩充性

### 条款24:若所有参数皆需类型转换，请为此采用non-member函数

* 如果一个函数的**所有参数**都可能涉及到**隐式类型转换**，为了实现**混合式运算**，我们应该将这个函数设计为non-member函数。
    - 因为只有当**参数被列于参数列（parameter list）内**，这个参数才是隐式类型转换的**合格参与者**
    - 成员函数：只有位于类对象的**右边部分的数据**才在成员函数的参数列表内部，才能进行隐式类型转换，左边则不行
    - 
    ```
    如operator*()是Rational成员函数：
    Ration x;
    x＊3;//合法
    3＊x;//不合法
    ```
    - **非成员函数**，则没有方向，**两边都在该函数的参数列表**内，都可以进行隐式类型转换。

#### 总结
* 如果你需要为某个函数的所有参数进行类型转换，那么这个函数必须是non-member

### 条款25:考虑写出一个不抛异常的swap函数

* 为什么我们要讨论swap函数：
    - 因为swap是异常安全性编程的脊柱，见条款29
    - 也是处理自我赋值可能性的一个常见机制，见条款11

* 分析STL提供的标准程序库中swap算法，实现如下：

```
namespace std{
    template<typename T>
    void swap(T &a,T &b){
        T temp(a);
        a = b;
        b = temp;
    }
}
```
* 该函数只对支持coping操作的类使用，并且要经过**三次拷贝或赋值**的操作，对于那些底层数据是由一个**指向实际数据的指针**所维护时，这样的操作就显得**效率很低**，因为理论上，我们只需要将两个类中的**指针进行交换**即可。

为此出于效率的考虑，我们尝试一下几种方案：

* 对于class，设计**全特化版本**的swap函数，当遇到该类（**底层用指针维护数据的类**）时，直接交换指针即可
    - 由于非成员函数**不能访问类的private 指针数据**，所以我们需要为该类添加一组swap**成员函数**
    - 成员函数利用指针数据，再**调用**标准版的swap函数
    - 所以特化版本调用成员函数，再调用标准版本进行指针交换
* 当如果是一个**template**，而不是class时，我们不能再为该template设计一个特化版的swap，因为对于**模版函数(function template)，C++规定只能进行全特化，不能进行偏特化**，不同于class template
    - 所以我们只能直接提供一个**重载版本**的swap模版函数，但**std是一个特殊的空间，不可以添加新的template到std**
        + 所以我们需要将该class temlate和重载版本的swap模版放到一个**新的namespace**中去，成为**专属**的swap版本函数

* **最后的实现方案**就是：为该class或template添加一个**专属**版本的swap函数，该函数调用一个**成员函数**，该成语函数结合指针数据**调用标准swap函数**进行指针交换：
    - 对于class,在**std命名空间**内添加一个**全特化版本**的swap函数，调用成员函数，成员函数调用标准swap函数进行指针交换
    - 对于template,在**新的命名空间**添加一个**重载版本**的swap函数，同样调用成员函数，成员函数调用标准swap函数进行指针交换

* 所以swap函数可能有**三个版本**：
    - std既有的**一般swap**版本
    - std中为class设计的**全特化**swap版本
    - 为template设计的**专属swap版本**，可能栖身在某个命名空间中
    - C++的**名称查找法则**会找到最适合的swap版本

* **成员版的swap**绝不可以抛出异常，因为swap的一个最好的应用就是帮助class提供**异常安全性**
* 对非成员版的swap没有这个约束条件


## 5.实现

这一章将就如下几个实现上可能出现的问题进行解释：

1. **太快定义变量**可能造成效率上的拖延
2. **过度使用转型**可能导致代码变慢又难以维护
3. 返回对象“**内部数据之号码牌**”可能会破坏封装并留给客户虚吊号码牌
4. 未考虑**异常**带来的冲击导致资源泄漏或数据败坏
5. 过度热心的使用**inlining**可能引起代码膨胀
6. **过度耦合**则可能导致令人不满意的冗长build时间

### 条款26:尽可能延后变量定义式出现的时间

此条款在对象模型中有提到过：[对象的构造和析构](http://blog.xbblfz.site/2018/04/21/C++%E5%AF%B9%E8%B1%A1%E6%A8%A1%E5%9E%8B#对象的构造和析构)

* 一个带有构造函数和析构函数变量对象，不要**过早定义**，因为很有可能在该对象还没有被使用之前，该函数就已经**提前退出**，这样就会导致**没有必要的对象构造和析构过程**。
* 并且通过"**default构造函数构造出一个对象然后对它赋值**"比“**直接在构造时指定初值**”效率差
* 所以我们应该尽可能**延后变量的定义**，不仅仅是非得使用该变量的前一刻为止，甚至应该延后到这份定义直到**能够可以指定给它初值实参**为止。这样可以**避免无意义的default** 构造行为

### 条款27:尽量少做转型动作

转型分类：

C风格转型动作：

    * (T)expression
    * T(expression)//函数风格

C++中四种**新式转型**：

    * const_cast:通常用来将对象的常量性移除
    * dynamic_cast:主要用于"继承关系安全向下转型",也是唯一可能耗费重大运行成本的转型动作
    * reinterpret_cast:意图执行低级转型，通常为算术对象的位模式提供较低层次上的重新解释。如将int转换成char。很危险！
    * static_cast:用来强迫隐式转换，唯一无法将const转换为non-const

相对于旧式的C风格转化动作，我们更**推荐新式转换**:

1. 更容易被辨识，清楚表达了转换动作
2. 窄化转换动作，每个动作用在不同的转换过程中，可以知道转换失败的原因。

**尽量少做转型：**

1. **许多程序员相信，转型其实什么都没做，只是告诉编译器把某种类型视为另一种类型。这是错误的观念，任何一种类型转换往往真的令编译器编译出运行期间执行的代码**

    * 如下面的代码:

    ```c++
    class Base{...};
    class Derived:public Base{...};
    Derived d;
    Base *pb = &d;//隐式地将Derived*转换为Base*
    ```

    * **会有个偏移量在运行期被实施于Derived*指针身上，用以取得正确的Base*地址**

2. **我们很容易写出似是而非的代码**

    ```c++
    class Window{
        public:
            virtual void onResize() {...}
            ...
        }
        //错误的做法
    class SpecialWindow: public Window{
        public:
            virtual void onResize(){
                static_cast<Window>(*this).onResize();  
                ...  //这里进行SpecialWindow专属行为
            }
            ...
        }
    //正确的做法
    class SpecialWindow: public Window{
        public:
            virtual void onResize(){
                Window::onResize();  //调用Window::onResize作用于*this身上
                ...  //这里进行SpecialWindow专属行为
            }
            ...
        }
    ```
    * 错误的做法中，static_cast<Window>(*this)会生成一个临时的副本对象，而该对象调用Resize函数是作用于该副本对象之上的
    * 正确的做法是直接调用基类Window的Resize()函数作用于本对象*this之上
3. **在继承关系中使用dynamic_cast进行类型转换效率低**
    * 使用dynamic_cast的场景：当我们想要掉用一个**派生类中的某个函数**的时候，但只有一个指向派生类的**基类指针**,我们需要进行该指针的类型转换来调用派生类中的函数
    * 由于dynamic_cast转换效率低，我们有两种方式来替换它并解决对应的场景问题
        - （1）使用容器存放指向派生类的指针（智能指针）
        - （2）使用虚函数机制
        - 一定要避免一连串的dynamic_casts。这样产生的代码又大又慢。

### 条款28:避免返回handles指向对象内部成分

考虑一个类设计的场景：一个类Rectangle的底层数据RectData由一个指针pData进行维护，该指针指向该类真正的数据内容

```c++
struct RecData{
    Point ulhc;//Point类不作描述.左上角点
    Point lrhc;//右下角的点
}

class Rectangle{
    ...
    private:
        std::tr1::shared_ptr<RecData> pData;//指针指向实际数据
    public:
    ... 
        Point &upperLeft() const(return pData->ulhc;)
        Point &lowerLeft() const(return pData->lrhc;)
}
```

上述的类Rectangle有两个成员函数返回指向内部数据的引用,则有：

```c++
const Rectangle rec(point1,point2);//构造一个常量矩形对象
rec.upperLeft().setX(50);//调用Point类中的setX函数修改坐标
```

* **问题**：明明我们的rec设计为**const矩形常量对象**，却依然可以修改其坐标值。这是因为底层数据的指针并没有修改，成员函数又直接返回底层数据的引用，所以可以修改

*  两个教训：
    -  （1）**成员变量**的封装最多只等于**“返回该变量引用”的函数**的访问级别，如果一个成员函数返回该成员变量的引用，且访问属性为public ,则就算该成员变量为private的，其封装性也只是public
    -  （2）如果const成员函数传出一个reference,后者所指数据与对象自身有关联，它又被存储在对象之外（指针的形式），那么那个函数的调用者可以修改那笔数据

* 解决思路：为了防止返回的内部数据被篡改，我们可以返回一个const引用
    - 这又引入一个新的问题，因为这样可能会出现一个"空悬"的handle

    ```c++
    class GUIObject{...}

    const Rectangle boundingBox(const GUIObject& obj);

    GUIObject * pgo;
    ...
    const Point * pUpperLeft = &(boundingBox(*pgo).upperLeft());
    ```
    - 上面的例子中，boundingBox函数返回了一个Rectangle的临时对象，返回该临时对像的内部数据的指针pUpperLeft后就被销毁，这就导致该指针成为“空悬指针”

* **所以避免返回handles（包括references、指针，迭代器）指向对象内部**
    - 可以增加封装性
    - 帮助const成员函数保持const性质
    - 降低发生“空悬”handle发生的可能性

### 条款29:为“异常安全”而努力是值得的

假设一个有个class用来表现夹带背景图案的GUI菜单，这个class希望用于多线程环境，所以它有一个互斥器作为“并发控制”之用：

```c++
class Menu{
    public:
        ...
        void changeBackground(std::istream& imgSrc);
        ...
    private:
        Mutex mutex;
        Image *bgImage;
        int imageChanges;
};

//一个可能的函数实现

void Menu::changeBackground(istream& src){
    lock(&mutex);
    delete bg;
    ++changeCount;
    bg = new Image(src);
    unlock(&mutex);
}

```

异常安全有**两个条件**,该函数changeBackground任何一点都没有达到要求：

* **不泄漏任何资源**
    - 函数中new Image()如果出现异常，锁资源就一直不会释放
* **不允许数据败坏**
    - 如果"new Image(imgSrc)"抛出异常，但其实背景并没有改变，但是记录改变次数的变量changeCount却发生了变化。

**对于资源泄漏问题**，我们可以参考条款13，使用**智能指针*管理资源，或单独设计一个**资源管理类**（因为有的资源不能仅仅通过delete释放，比如这里的锁）

**对于数据败坏，见下文**

异常安全函数提供以下三个保证之一：

1. **基本承诺**：如果异常被抛出，保证函数内部的数据对象依然**满足所有数据约束条件**，可能保持原数据，也可能为缺省的数据。
2. **强烈保证**：如果异常被抛出，保证函数内的数据对象会和**调用该函数之前一样**
3. **不抛掷保证**：这是最强的保证，函数总是能完成它所承诺的事情（作用于内置类型身上的所有操作都提供nothrow保证。这是异常安全代码中一个必不可少的关键基础）

```c++
class Menu{
    shared_ptr<Image> bg;
    ...
};
void Menu::changeBackground(istream& src){
    Lock m1(&m);
    bg.reset(new Image(src));
    ++changeCount;//先改变图像背景后，在递增改变次数
}
```

* 上述函数实现，只提供了对**Menu对象的“强烈保证”**，对全局状态没有“强烈保证”。因为参数src为一个**流迭代器**，当new Image操作会**移动**流迭代器的**读取指针**，所以src的状态发生了变化，当changeBackground发生异常时**并不能保证src恢复到之前的状态**

**copy and swap策略:为你打算修改的对象做一个副本，然后在副本上进行修改，若修改过程出现异常，原数据不会变动，当修改成功时，将副本和原对象在一个不抛出异常的操作中置换**

```c++
class Menu{
    ...
private:
    Mutex m;
    std::shared_ptr<MenuImpl> pImpl;
};
Menu::changeBackground(std::istream& src){
    using std::swap;            // 见 Item 25
    Lock m1(&mutex);            // 获得mutex的副本数据

    std::shared_ptr<MenuImpl> copy(new MenuImpl(*pImpl));
    copy->bg.reset(new Image(src)); //修改副本数据
    ++copy->changeCount;

    swap(pImpl, copy);              //置换数据，释放mutex
}
```

copy and swap策略能够为对象提供异常安全的“强烈保证”。但是一般而言，它并不保证整个函数有“强烈保证”。也就是说，如果某个函数使用copy and swap策略为某个对象提供了异常安全的“强烈保证”。但是这个函数可能调用其它函数，而这些函数可能改变一些全局状态（如数据库状态），那么”整个函数“就不是”强烈保证“

> 函数提供的”异常安全保证“通常最高只等于其所调用的各个函数的”异常安全保证“中的最弱者

#### 异常安全的选择

当”强烈保证“不切实际时（比如前面提到的全局状态改变难以保证，或者效率问题），就必须提供”基本保证“。现实中你或许会发现，可以为某些函数提供强烈保证，但效率和复杂度带来的成本会使它对许多人而言摇摇欲坠。只要你曾经付出适当的心力试图提供强烈保证，万一实际不可行，使你退而求其次地只提供基本保证，任何人都不该因此责难你。对许多函数而言，”异常安全性的基本保证“是一个绝对同情达理的选择

总的来说就是，应该为自己的函数努力实现尽可能高级别的异常安全，但是由于种种原因并不是说一定需要实现最高级别的异常安全，而是应该以此为目标而努力

### 条款30:透彻了解inlining的里里外外

* inlining的优势：
    - 免去函数调用的开销
    - 编译器对函数可以进行优化
* 劣势：代码体积大


* inline是对编译器的一个**申请**而不是**强制命令**，这项申请可以隐喻提出，也可以明确提出
    - 隐喻提出：将函数定义于class定义式内，可以是成员函数，或友元函数
    - 显示提出：inline关键字
    - inline是一个申请
        + 编译器会分析该函数是否过于复杂,从而拒绝inline
        + 并且所有对virtual函数（执行期才确定的）的 inline（编译期）申请都会被拒绝
* 不对**“通过函数指针的调用”**实施inlining操作，所以对inline函数的调用有可能被inlined，也有可能不被inlined
* template函数可以和inline结合使用，因为template在**编译时期就会具现化**。
* **构造函数和析构函数**往往是inlining的糟糕候选人，因为编译器对构造函数和析构函数都会**安插代码**执行相应的动作(成员变量初始化，vptr设置等等。。)。并且比较复杂，并不适合inline
    - 代码安插可以见C++对象模型中构造函数语意学和析构函数语意学

#### 总结
将大多数inlining限制在**小型、被频繁**调用的函数身上，这使得日后的调试过程和二进制升级更容易，也使得潜在的代码膨胀问题最小化，程序速度提升最大化

### 条款31:将文件间的编译依存关系降到最低

问题场景：当我们对C++程序中某个class实现文件做了些**轻微修改**，然后重置这个程序，本来预计只需要花数秒就好，毕竟只有一个class被修改，然后我们发现**编译的过程非常庞大**，仿佛整个程序被重新编译了。

原因：没有很好实现“将接口从实现中分离”，导致"定义文件"和其**#include含入文件**形成一种编译依存关系

举例：

```
#include<string>
#include "date.h"
#include "address.h"

class Person{
public:
    ...
private:
    std::string theName;    //实现细目
    Date    theBirthDate;   //实现细目
    Address theAddress;     //实现细目
};
```

Person类定义和include含入文件"data.h","address.h"具有编译依存关系。所以只要头文件date.h或address.h中Date和Address类有一点改变，那么每一个含入Person class的文件都要重新编译。

* 实现接口从实现中分离的两个方案：
    - handle class
    - interface class

* handle class在一个类的指针指向该类的实现类，使得“声明的依存性”替换“定义的依存性”。尽量让头文件"自我满足":

1. 使用一个类作为接口
2. 另外实现一个实现类
3. 接口类中的函数都是通过调用指针所指向的实现类中相应函数实现的。
4. 因为编译过程中，编译器需要知道一个类的大小，所以当接口类和实现类分离后，接口类的大小可以确定（因为是一个固定大小的指针指向实现类），所以实现类中出现改动并不需要重新编译接口类。同样接口类出现小变动也不用重新编译实现类

```c++
class Person{
public:
    Person(string& name);
    string name() const;
private:
    shared_ptr<PersonImpl> pImpl;//指向实现类的指针
};
Person::Person(string& name): pImpl(new PersonImpl(name)){}
string Person::name(){
    return pImpl->name();//接口函数转调用实现类中的函数
}
```
为了实现接口和实现的分离，我们要为**声明式和定义式**提供不同的头文件。

* interface class :抽象基类，这类通常没有成员变量，也没有构造函数，只有一个virtual析构函数，和一组pure virtual函数来叙述整个接口。
    - 抽象基类不能被实例化，但是其派生类可以被实例化，所以我们需要在抽象基类提供一个**工厂函数来实例化派生类**(调用派生类的构造函数)，并且该函数往往是static

* 实现handle class和interface class从而将接口和实现分离也是需要付出一定代价的：
    - handle class：访问出现间接性，内存消耗，动态内存分配后回收带来的消耗
    - interface class:每次函数调用经过间接跳跃的成本，vptr指针的成本

#### 总结

* 支持编译依存性最小化的一般构想是，相依于声明式而不是定义式，定义式应该隐藏在声明式后面，声明式和定义式头文件尽量能自我满足
* 程序库头文件应该以“完全且仅有声明式”的形式存在





## 6.继承与面向对象设计

### 条款32: 确定你的public继承塑膜出is-a关系

* "public继承"意味is-a,适用于于base classes身上的每一件事情也一定适用于derived classed身上，因为每一个derived class对象也都是一个base class
* 除了is-a关系，另外两个常见的关系是has-a和is-implemented-in-terms-of

### 条款33:避免遮掩继承而来的名称

* 派生类中的名称会完全覆盖基类中相同的名称
    - 如果我们在覆盖基类名称的情况下想要使用**基类中的该同名函数**，可以使用**using声明**使得基类中的该名称在派生类中也可见
    - 使用using 声明会将该名称的所有重载版本都在派生类中可见，但是我们只想**继承基类中某一特定的版本**，我们可以使用**转交函数**
        + 这种**不想继承base class内的所有函数**的现象不可能在public继承中发生，因为条款32所说，这会违反派生类和基类之间is-a关系

转交函数：

```c++
    class Base{
     public:
        virtual mf1() = 0;
        virtual mf1(int);
    };
    class Derived:private Base{//继承为private继承
    public: 
        virtual void mf1(){
            Base::mf1();//定义一个同名函数，调用基类版本中的指定版本函数
        }
    };
```


### 条款34:区分接口继承和实现继承

* 身为class的设计者，有时候你希望：
    - （1）派生类只继承成员函数的**接口**
    - （2）派生类同时继承成员函数的**接口和实现**，但又**希望覆写**该实现
    - （3）派生类同时继承成员函数的**接口和实现**，但**不允许覆写**任何东西

* 对于情形1，我们可以使用**纯虚(prue virtual )函数**来实现
* 情形2:
    - 对于情形2，我们使用**impure virtual（没有加=0）** 函数，就是在**类外**提供了一份定义（该接口的**缺省实现**）。
        + 此时若派生类没有声明该虚函数，则继承默认实现
        + 但派生类也可以声明该虚函数，覆写该实现
    - 对于情形2,我们还可以使用**pure virtual(加＝0)**函数，然后在类外提供缺省定义实现
        + 此时派生类必须**强制重新声明**虚函数，然后**调用**基类该虚函数的默认实现
        + 也可以自己提供一份实现
* 对于情形3，我们则**不使用虚函数**，在类内直接定义，为了让派生类继承函数的**接口和一份强制性的实现**

#### 总结

1. 接口继承和实现继承不同，在**public继承**之下，derived classes总是继承base class的接口
2. pure virtual函数只具体指定接口继承
3. 简朴的（非纯）impure virtual函数具体指定接口继承及缺省实现继承
4. non-virtual函数具体指定接口继承以及强制性实现继承

### 条款35:考虑virtual函数以外的其他选择

一共有四种方案可以替代virtual函数：

1. Non-Virtual Interface(NVI)手法实现Template Method模式

    * 该模式就是将虚函数包装在一个public的非虚函数中调用
    * 虚函数设置为private（也可以是protected,供派生类调用）,派生类中可以重复声明和定义
    ```c++
    class GameCharacter{
        public:
        //non-virtual函数，virtual函数的包裹器(wrapper)
        int healthValue() const
        {
            ...                             //做一些事前工作
            int retVal = doHealthValue();   //负责真正的健康值计算
            ...                             //做一些事后工作
            return retVal;
        }
        ...
    private:
        virtual int doHealthValue() const   //派生类可以重新定义
        {
            ...     //缺省的健康值计算方法
        }
    };
    ```
    * NVI模式的主要有点在于可以在虚函数的包裹层public非虚成员函数中，做一些**“事先”（**加锁，数据越是等）和**“事后工作”**（释放锁，验证数据约束）
2. 使用Function Pointers实现Strategy模式：在基类添加一个**函数指针**，然后通过调用派生类的**构造函数**，传递适合各派生类版本的函数指针。调用不同的函数
    * 这样可以实现同一类型对象调用**不同**的函数
    * 某已知类型的函数也可以在**运行期**进行替换，添加一个**设置该指针的成员函数**
    * 这种指针只适用于那些可以**通过public接口**来实现，而不需要**non-public信息**的函数实现
        - 解决这个限制的方法，就是将指针指向的非成员函数设置为**友元**
3. 使用可调用对象**std::tr1::function<>**来替换方案2中的指针，这相当于是方案2的**泛化版本**，提高兼容性
4. 古典的Strategy模式，将virtual函数替换为**另一个继承体系**内的virtual函数，也就是类中添加一个指向其他继承体系类的基类指针（可以指向任何派生类）,通过该指针调用对应类的虚函数。然后我们构造派生类时，传入对应另一个继承体系中对应派生类的指针即可实现虚函数的多态。

### 条款36:绝不重新定义继承而来的non-virtual函数

```c++
class B{
    public:
        void mf();
        ...
};

class D:public B{...};

D x;
//下面的两个对mf的调用行为不一致
B* pB = &x;
pB->mf();

D* pD = &x;
pD->mf();
```

* 上述通过指针pB和pD指针调用的mf并不是同一个函数，即使pB和pD指向同一个对象x
    - 因为非虚函数，都是**静态绑定**的，在编译期就已经确定是调用哪一个版本。
    - 对于虚函数就不存在这样的问题，因为是**动态绑定**
* 如果重新定义继承而来的non-virtual函数，会破坏public继承的is-a关系：说明每一个D都不是一个B。 

### 条款37:绝不重新定义继承而来的缺省参数值

* 因为virtual函数是动态绑定，缺省参数则是静态绑定：
    - 静态绑定：前期绑定，静态类型就是它在程序中被声明的类型
    - 动态绑定：后期绑定，动态类型就是当前所指对象的类型

```c++
class Shape{
    public:
        enum ShapeColor{Red,Green,Blue};
        virtual void draw(ShapeColor color = Red) const = 0;
        ...
}

class Rectangle:public Shape{
    public:
        //赋予不同的缺省值
        virtual void draw(ShapeColor color = Green) const;
        ...
};

class Circle:public Shape{
    public:
        vrtual void draw(ShapeColor color) const;
        ...
};
//考虑如下指针

Shape* ps;                  //静态类型为Shape*
Shape *pc = new Circle;     //静态类型为Shape*
Shape* pr = new Rectangle;  //静态类型为Shape*
```

* ps，pc,pr静态类型均为Shape*,pc动态类型为Circle*,pr动态类型为Rectangle*,ps没有动态类型。
* pc**没有指定默认参数值**，所以pc->draw调用的是Circle::draw(Red)。从Shape那里**继承默认参数Red**,因为是**静态绑定**。
* **pr有默认参数**，pc->draw()调用的是Rectangle::draw(Red)。这里的默认参数不是Rectangle的Green，而**是Shape中的Red**,也是因为默认参数是**静态绑定**的。
* C++为什么要使用这样的方式：缺省值静态绑定而不是动态绑定，是为了简化编译器的实现，不影响程序的运行速度。

### 条款38:通过复合塑膜出has-a或“根据某物实现出”

* public继承是一种is-a关系，复合关系有两层含义：
    - has-a有一个,如Person有一个地址
    - "is-implemented-in-terms-of"根据某物实现出,如set由list实现
* 复合关系的实现，就是将一个类作为另一个类的成员变量（或指针）即可

### 条款39:明智而审慎地使用private继承

```c++
class Person{...};

class Student:private Person{...};//private继承Person

void eat(const Person& p );
void study(const Student &s);

Person p ;
Student s;
eat(p);   //没问题，人会吃
eat(s);   //错误，学生不会吃？

```

* 上述代码我们可以看到，Student private继承自Person类，但是**编译器不能自动将Student类转换为Person类**，这和public继承有所不同，这也是为什么eat(s)会调用失败的原因
* 由private base class继承而来的所有成员，在derived class中都会**变成private属性**，纵使它们在base class中**原本是protected和public属性**
* 并且private继承中的派生类**可以重新定义**base class中的private虚函数，但**不能调用**它。


* Private继承的含义：is-implemented-in-terms-of（根据某物实现）,如果class D private继承class B,说明D需要利用B的部分实现技术或特性来实现
    - Private继承也就是意味着只有**实现部分**被继承，**接口部分**被省略（不继承接口，只**使用基类中被实现的接口**）。
    - private继承在软件**“设计”层面**上没有意义，其意义只及于软件**实现层面 **

* 复合也是意味着"is-implemented-in-terms-of",如何在复合技术和private继承中做取舍
    - 尽可能使用复合技术
    - 必要时才使用private继承，必要的时候是指：
        + 涉及到protected成员或虚virtual函数时（因为复合技术中**不能访问protected**成员函数，也**不能重写部分private虚函数**）
        + 激进的情况：**节约空间**，当复合关系结合**empty class**，编译器会**安插一个字节**，但是当继承一个empty class时，编译器会使用**EBO（空白基类最优化）技术**，派生类不会有多的字节插入。

**必要情况1：想重新定义virtual函数**

* 当一个类想要**重新定义**另一个类中的虚函数时，并且具有is-implemented-in-terms-of关系，这时就必须使用private继承。
* 如Widget(窗口，要实现定时审查每个成员函数被调用的次数)，要**利用Timer类来实现定时操作**
* 但我们**不能使用public继承**，因为Timer类的成员函数onTick就可以通过Widget来调用。但很明显onTick在**观念上**不应该属于Widget类的一部分。但我们需要利用它来实现。
* 所以我们使用**private继承**,这样可以继承Timer然后**重新定义**onTick虚函数

```c++
class Timer{
    public:
        explicit Timer(int tickFrequency);
        virtual void onTick() const;
}

class Widget:private Timer{
    private: virtual void onTick() const;//重新定义
}
```

* 如果想要**阻止Widget派生类再重新定义**Timer 的onTick()函数，我们可以使用**嵌套类（潜逃类中重新定义）**，然后让该潜逃类成为Widget的**private成员**，这样派生类就**无法访问该成员对onTick重新定义**了。
    - 为了让Widget的**编译依存性降到最低**，应该将WidgetTimer**移出**到Widget之外，Widget**内含一个指向WidgetTimer的指针**。Widget只需要WidgetTimer的**声明式**即可

```c++

class Widget{
    private:
        class WidgetTimer:public Timer{//潜逃类，来重新定义虚函数onTick
            public:
                virtual void onTick() const
                ...
        };
        WidgetTimer timer;//做成员变量
}
```

**必要情况2：节约空间**

* 编译器往往对一个没有数据，没有**non-static成员变量，没有virtual函数（vptr）,也没有virtual base class(也会有指向虚基类的指针导致额外开销)**的空类，安插一个**char**到该对象中。

```c++
class Empty{};

class HoldsAnInt{
    private:
        int x;
        Empty e;//直观上不需要任何内存
}
```

* 复合实现中，在HoldsAnInt类中，其对象大小为8字节，int占4字节，e为1字节，又因为**内存对齐**的要求，所以一共4+4为8字节

```c++
class HoldsAnInt{
    private:
        int x;
};
```

* 当使用private继承时，HoldsAnInt类就只有4字节了，因为**EBO(empty base optimization,空白基类最优化)**
    - EBO一般**只在单一继承下**可行，在多重继承下不可行

#### 总结

1. Private继承也就是意味着is-implemented-in-terms-of,它通常比复合技术的级别低，但是当派生类要**访问protected base class的成员**时，或需要**重新定义继承而来的virtual函数**时，这门设计使用private继承是合理的
2. 和复合技术不同，private继承可能造成**empty class的最优化**

### 条款40:明智而审慎地使用多重继承

* 多重继承容易导致函数调用或**成员访问的歧义**，因为一个类可能继承不同类中的相同名称，当访问该名称时，不知道是访问哪个类中的成员
    - 当一个类继承自两个类时，且这两个类有**相同名称的成员**时，即使一个为public，一个为private,也会出现歧义，因为函数匹配通常**先找到最佳匹配函数，然后再检验其可用性 **
* 当出现钻石继承的情况时，一个基类可能在派生类中有**多份复制**，为防止这种情况出现，应使用**虚基类**
    - 虚继承可能带来增加大小，速度，初始化复杂度等等成本，如果虚基类不带任何数据，将是最具有实用价值的情况
    - 多重继承的确有正当用途，其中一个情节涉及“**public继承**某个interface class（is-a 该关系）”和"**private 继承**某个协助实现的class（is-implemented-in-terms-of）"的两相组合

## 7.模型与泛型编程

### 条款41:了解隐式接口和编译期多态

* 面向对象编程世界总是以显示接口和运行期多态解决问题：
    - 显示接口：就是那些在类中已经声明和定义实现的函数接口
    - 运行期多态：就是virtual函数在运行期才能确定调用哪一个版本的函数

* 模版和泛型编程的世界则是隐式接口和编译期多态更加重要，但也存在显示接口和运行期多态
    - 隐式接口：比如一个模版函数中类型T，我们通过T调用部分函数，这些函数接口都是T**必须要支持的隐式接口**，这些隐式接口都是基于有效表达式
    - 编译器多态：因为传入不同的类型，这些隐式接口的实现也都不一样，并且这些隐式接口会在编译期全部具现，确定**调用不同版本的函数**

### 条款42:了解typename的双重意义

1. 在声明template参数时，前缀关键字class和typename可以互换
2. 请使用关键字typename标示**嵌套从属类型**名称，但不得在base class lists或member initialzation list内以它以它作为base class修饰符

对于意义2，其中涉及到从属名称，嵌套从属名称，非从属名称的三个概念，typename只能用于指明嵌套从属名称：

* 从属名称：template内部出现的名称相依于某个模版参数名称C
* 嵌套从属名称：名称涉及到参数C或包含C的类型内部的某个成员，嵌套模式C::const_iterator，Base<C>::Nested
* 非从属名称：像int 类型的名称，与模版参数无关的名称


* 因为编译器在模版没有具现化的时候并**不知道模版参数C是什么类型**，但此时我们却要访问它的内部成员，所以我们需要通过typename来**通知编译器该名称C是一个类类型**。

* **并且typename不能出现在派生列表和成员初始化列表中:**

```c++
template<typename T>
class Derived:public Base<T>::Nested{//派生类列表中不能使用typename
    public:
        explicit Derived(int x):Base<T>::Nested(x){//成员初始化列表中不能使用typename
            typename Base<T>::Nested temp;//这里需要使用typename 
            ...
        }
        ...
};
```

typename 可能对移植性上有一定麻烦

### 条款43:学习处理模版化基类内的名称

我们设计一个模版，来完成给不同Company发布信息操作：

```
template<typename Company>
class MsgSender{
public:
    ...
    //1.发送原始文本
    void sendClear(...)
    {
        ...
        Company c;
        c.sendCleartext(...);  //Company有一个成员函数实现信息发布
    }
    //2.发送加密后的文本
    void sendSecret(...) {...}
    ...
};
```

我们想要添加一个带有日志记录的发布信息的模版类，继承自MsgSender:

```c++
template<typename Company>
class LoggingMsgSender: public MsgSender<Company>{
        ...
    void sendClearMsg(...)
    {
        //将“传送前“的信息写至log；
        sendClear(...);             //调用base class函数，无法通过编译
        //将”传送后“的信息写至log；
    }
    ...
};
```

LoggingMsgSender想要调用基类的sendClear成员函数却被编译器拒绝编译，为什么？

* 因为模版MsgSender可能会被**全特化**，而该全特化的版本可能没有成员函数sendClear;
* 所以编译器**拒绝在模版化的基类**中寻找继承而来的名称

为了让编译器能进入到模版化的基类中寻找继承而来的名称，我们有三种解决办法：

1. **this->**sendClear();使用 this来告诉编译器
2. **using声明**：使用using MsgSender<Company>::sendClear;using声明告诉编译器假设基类中有该成员函数
3. **静态调用**该成员函数 MsgSender<Company>::sendClear(info);
    * 这样调用有一个缺点：如果被调用的是虚函数，往往就会**关闭virtual的动态绑定**行为


* 虽然这样可以让编译器尝试去基类中寻找名称，但是当模版具现化时，发现没有该成员函数，晚期依然会报错,所以编译器宁愿早一点出错，不去基类中寻找相应名称。


### 条款44:将与参数无关的代码抽离templates

产生重复代码：

```c++
template<typename T,std::size_t n>
class SquareMatrix{
    public:
        void invert();
}

SquareMatrix<double,5> sq1;
SquareMatrix<double,10> sq2;
```

上述的sq1和sq2具现化了两份模版代码，对于invert函数只有矩形大小n的不同，却**产生了两份invert重复代码**，这种现象就是**代码膨胀**。

这个概念其实和**函数的设计**类似，当我们有两个函数要实现时，但实现部分具有重复的步骤，为了减少重复代码的生成，我们将重复的部分提出来放到一个新函数中去，然后再调用该新函数即可

同样我们将两个模版的重复的部分invert函数提取出来到基类模版：

```c++
template<typename T>
class SquareBase{
protected:  
    //以下函数只是作为避免代码重复的方法，并不应该被外界调用，
    //同时，该函数希望被子类调用，因此使用protected
    void invert(int size);
};
template<typename T, int n>
class SquareMatrix:private SquareBase<T>{//只要T相同，都会使用同一份父类实例，
private:                           //因此，只有一份invert(int size)
    using SquareBase<T>::invert;
public:
    //调用父类invert的代价为零，因为Square::invert是隐式的inline函数
    void invert(){ this->invert(n); }
}

```

上面的代码我们可以看到对于相同类型T的不同SquareMatrix模版共享一个SquareBase模版，和**同一份invert函数代码**。SquareBase模版只有一份，只与类型相关T:SquareBase<double>

优势：降低程序工作集（working set）,因为运行在内存中的重复代码减少，并且强化高速缓存区的引用集中化

* 对于类型模板参数产生的代码膨胀，可以让不同实例化的模板类共用同样的二进制表示
    - int和long在多数平台都是一样的底层实现，然而模板却会实例化为两份，因为它们类型不同
    - List<int *>, List<const int *>, List<double *>的底层实现也是一样的。但因为指针类型不同，也会实例化为多份模板类 如果某些成员函数操作强型指针(T*)，应该令它们调用另一个操作 **无类型指针(void\*)的**函数(vector,deque,list都是这样实现的)，后者完成实际工作


### 条款45:运用成员函数模版接受所有兼容类型

原生指针可以很好地支持隐式转换，派生类指针可以转换为基类指针，指向non-const对象的指针可以转换为指向const对象等，但是对于用户自定义的智能指针如何实现这些隐式转换？

* 通过拷贝构造函数

我们将智能指针设计为一个模版，进行隐式转换时，我们传入要转换的模版对象到拷贝构造函数：
* 假设我们要将SmartPtr<Middle>隐式转换为Smart<Top>,我们需要设计一个**拷贝构造函数**，该构造函数的参数为SmartPtr<Middle>的引用
    - 这样则模版需要为每一个派生类Middle,Bottom,BelowBottom...都设计一个构造函数
    - 为了解决这一问题，我们将构造函数设置为一个**模版成员函数**如下：

```c++
class Top {...};
class Middle : public Top {...};
class Bottom : public Middle {...};

template<typename T>
class SmartPtr{
public:
    //构造函数模板
    //意思是：对任何类型T和任何类型U，可以根据SmartPtr<U>生成一个SmartPtr<T>
    template<typename U>
    SmartPtr(const SmartPtr<U> &other)
     : heldPtr(other.get()) {...}
    //原始指针为private成员，需要一个接口来获取
    T* get() const {return heldPtr;}
    ...
private:
    T* heldPtr;   //智能指针所持有的原始指针
};
```

这样就可以兼容所有类型的智能指针都能进行隐式转换。不需要为每个派生类定义多个构造函数。依然有一个问题，这样会导致Top类型的智能指针也可以向底层Middle进行隐式转换，这是不被允许的，所以我们可以通过**原生指针的隐式转换**heldPtr(other.get())来限制转换的方向。

最后需要指明的是：member templates并不改变语言规则，而语言规则说，如果程序需要一个copy构造函数，你却没声明它，编译器会为你暗自生成一个。因此，使用member templates实现一个泛化版的copy构造函数时，编译器也会合成一个“正常的”copy构造函数


### 条款46:需要类型转换时请为模版定义非成员函数

在条款24中，我们看到为什么只有非成员函数才能支持对所有参数实行隐式类型转换。所以同理对于模版，需要对所有参数实行隐式转换，我们需要定义一个非成员模版函数，来支持如*这种混合运算。

```c++
template<typename T>
class Rational{
public:
    Rational(const T &numerator = 0, const T &denominator = 1);
    const T numerator() const;
    const T denominator() const;
    ...
};

template<typename T>
const Rational<T> operator*(const Rational<T> &lhs,const Rational<T> &rhs)
{ ... }

Rational<int> oneHalt(1,2);
Rational<int> result = oneHalf * 2   //编译错误
```

* 然而我们发现onHalf*2的运算过程没有通过编译，这是因为：**template实参推导过程中从不将隐式类型转换函数纳入在内**
    - 推导第一个参数时，我们可以推断第一个参数lhs的类型为Rational< int >,因为onehalf就是一个Rational< int >
    - 但是第二个参数2，就无法推断出T为int ,因为推断过程不能将隐式转换纳入在内，这不同于一般的函数中的参数推导。
        + 为什么不能隐推导，因为第二个参数Ration<T>模版还没有具现化，更不可能调用模版内部的类型转换函数

* 解决办法：将该函数声明为Rational的**友元**，因为推断第一个参数得到T的类型后，**class模版根据该T直接具现化(class template不依赖template的参数推导，function template需要依赖)**，具现化过程也**包含友元函数的具现化**，但友元函数operator*具现化后就是一个普通函数，我们就可以根据**一般函数的参数推导过程**，将2进行隐式转换，调用该函数：


```c++
template<typename T>
class Rational{
public:
    ...
    //也可以是Rational<T>，但是省去<T>更简洁
    friend const Rational operator*(const Rational &lhs,const Rational &rhs)
    {
        return Rational(lhs.numerator() * rhs.numerator,
                         lhs.denominator() * rhs.denominator());
    }
};

```

* 如果我们在类的内部只声明该友元函数，在类的外部实现该函数，编译时会报出**连接错误**。**不能依赖类外的operator\* template提供定义**，我们必须自己在类内定义。

* 又由于类内的函数都是**inline函数**，当该函数的实现**过于复杂**时，为了减少对inline的冲击和代码膨胀，我们可以在类外部实现**辅助函数**，友元函数调用该辅助函数即可。

```c++
template<typename T> class Rational;

//helper template
template<typename T>
const Rational<T> doMultiply(const Rational<T>& lhs, const Rational<T>& rhs);

template<typename T>
class Rational{
public:
    friend Rational<T> operator*(const Rational<T>& lhs, const Rational<T>& rhs)
    {//通过友元来实现混合运算
        return doMultiply(lhs, rhs);
    }
};
```

* 辅助函数doMultiply 是无法支持混合运算的，但是被包裹在友元函数中，该友元函数支持混合运算。

### 条款47：请使用traits classed表现类型信息

1. Traits classed 使得“类型相关信息”在编译期可用。它们以temlates和“template特化”完成实现
2. 整合重载技术后，traits classed有可能在编译期对类型执行if...else测试

详见[STL源码解析中的介绍](http://blog.xbblfz.site/2018/05/28/STL%E6%BA%90%E7%A0%81%E5%89%96%E6%9E%90/#35%E8%BF%AD%E4%BB%A3%E5%99%A8%E5%9E%8B%E5%88%ABiterator_category#3.迭代器概念与traits编程技法)

### 条款48:认识template元编程


* Template metaprogramming(TMP)是编写template-based C++程序并执行与编译期的过程
* Template metaprogram(模板元程序)是以C++写成、执行于C++编译器内的程序

**TMP的两个重要特点：1）基于template；2）编译期执行**

TMP有2个伟大的效力：

1. 它让某些事情更容易。如果没有它，那些事情将是困难的，甚至不可能的
2. 执行于编译期，因此可将工作从运行期转移到编译期。会导致以下几个结果
    + 某些原本在运行期才能侦测到的错误现在可在编译期找出来
    + 使用TMP的C++程序可能在每一方面都更高效：较小的可执行文件、较短的运行期、较少的内存需求
    + 编译时间变长了

traits解法就是TMP，traits引发“编译器发生于类型身上的if...else计算”

另一个TMP的例子是循环，TMP并没有真正的循环构件，所以循环效果藉由递归完成。TMP的递归甚至不是正常种类，因为TMP循环并不涉及递归函数调用，而是涉及“递归模板具现化”。以计算阶乘为例子：

```c++
template<unsigned n>
struct Factorial{    //一般情况，Factorial<n>的值是n乘以Factorial<n-1>
    enum {value = n * Factorial<n-1>::value};
};

template<>
struct Factorial<0>{    //特殊情况：Factorial<0>的值是1
    enum {value = 1;}
};

int main()
{
    std::cout << Factorial<5>::value;    //打印120
    std::cout << Factorial<10>::value;   //打印3628800
}
```

TMP能够达到以下目标（这部分可以等有实际需求了再去详细了解）：

* 确保量度单位正确
* 优化矩阵运算
* 可以生成客户定制的设计模式实现品

## 8.定制new与delete

* operator new 和 operator delete用来分配单一对象
* Arrays所用的内存由operator new\[\]分配出来，并由operator delete\[\]归还
* STL容器使用的heap内存由容器所拥有的分配器对象管理

## 条款49：了解new-handler的行为

operator new抛出异常以反映一个未获满足的内存需求之前，会先调用一个客户指定的错误处理函数，new-handler，可以通过调用```std::set_new_handler()```来设置，```std::set_new_handler()```定义在\<new\>中：

```c++
namespace std{
    typedef void (*new_handler)();
    new_handler set_new_handler(new_handler p) throw(); 
    //以上，throw()是一个异常声明，括号内无任何内容，表示不抛任何异常
}
```

当operator new无法满足内存申请时，它会不断调用new-handler函数，直到找到足够内存。一个设计良好的new-handler函数必须做以下事情；

* **让更多内存可被使用**：一个做法是程序一开始执行就分配一大块内存，而后当new-handler第一次被调用，将它们还给程序使用。这便造成operator new内的下一次内存分配动作可能成功
* **安装另一个new-handler**：如果当前new-handler无法取得更多可用内存，可用安装另一个，下次operator new时会调用新的new-handler
* **卸除new-handler**：将null指针传给set_new_handler
* **抛出bad_alloc(或派生自bad_alloc)的异常**：这样的异常不会被operator new捕获，因此会被传播到内存索求处
* **不返回**：通常调用abort或exit（abort会设置程序非正常退出，exit会设置程序正常退出，当存在未处理异常时C++会调用terminate， 它会回调由std::set_terminate设置的处理函数，默认会调用abort）

### 实现class专属的new-handlers

```c++
class NewHandlerHolder{
public:
    explicit NewHandlerHolder(std::new_handler nh): handler(nh){}
    ~NewHandlerHolder(){ std::set_new_handler(handler); }
private:
    std::new_handler handler;
    NewHandlerHolder(const HandlerHolder&);     // 禁用拷贝构造函数
    const NewHandlerHolder& operator=(const NewHandlerHolder&); // 禁用赋值运算符
};

template<typename T>
class NewHandlerSupport{
public:
    static std::new_handler set_new_handler(std::new_handler p) throw();
    static void * operator new(std::size_t size) throw(std::bad_alloc);
private:
    static std::new_handler current;   //class专属的new-handlers
};

//class专属的new-handlers初始化为null
template<typename T>
std::new_handler NewHandlerSupport<T>::current = 0;

template<typename T>
std::new_handler NewHandlerSupport<T>::set_new_handler(std::new_handler p) throw(){
    std::new_handler old = current;
    current = p;    //将class专属的new-handlers设置为新的new_handler
    return old;     //返回旧的class专属的new-handlers
}

//new时会调用该operator new
//它会设置全局的new-handlers为该class专属的new-handlers，然后调用全局operator new申请内存
//h对象销毁后，其析构函数会将全局new-handlers恢复为调用前的状态
template<typename T>
void * NewHandlerSupport<T>::operator new(std::size_t size) throw(std::bad_alloc){
    NewHandlerHolder h(std::set_new_handler(current));
    return ::operator new(size);
}
```

有了```NewHandlerSupport```这个模板基类后，给Widget添加”new-handler”支持只需要public继承即可:

```c++
class Widget: public NewHandlerSupport<Widget>{ ... };
```

NewHandlerSupport的实现和模板参数T完全无关，添加模板参数是因为handler是静态成员，这样编译器才能为每个类型生成一个handler实例

### nothrow new

1993年之前C++的operator new在失败时会返回null而不是抛出异常。如今的C++仍然支持这种nothrow的operator new

```c++
Widget *p1 = new Widget;    // 失败时抛出 bad_alloc 异常
if(p1 == 0) ...             // 这个测试一定失败

Widget *p2 = new (std::nothrow) Widget;
if(p2 == 0) ...             // 这个测试可能成功
```

nothrow new只能保证所调用的nothrow版的operator new不抛出异常，但是构造也属于new的一个步骤，而它没法强制构造函数不抛出异常，所以并不能保证new (std::nothrow) Widget这样的表达式绝不导致异常

## 条款50：了解new和delete的合理替换时机

一般出于下列原因可能想要替换编译器提供的operator new或operator delete：

* 为了检测运用错误
* 为了收集动态分配内存的使用统计信息
* 为了增加分配和归还的速度
* 为了降低缺省内存管理器带来的空间额外开销
* 为了弥补缺省分配器中的非最佳齐位
* 为了将相关对象成簇集中
* 为了获得非传统的行为

下面是一个”为了检测运用错误“而实现的简单的operator new的例子，通过在首部和尾部插入一个签名，返回中间内存块给程序使用，如果程序在使用内存时发生过在区块前或区块后写入的行为，那么签名就会被修改，因此可以检测这种行为：

```c++
static const int signature = 0xDEADBEEF;    // 边界符
typedef unsigned char Byte; 

void* operator new(std::size_t size) throw(std::bad_alloc) {
    // 多申请一些内存来存放占位符 
    size_t realSize = size + 2 * sizeof(int); 

    // 申请内存
    void *pMem = malloc(realSize);
    if (!pMem) throw bad_alloc(); 

    // 写入边界符
    *(reinterpret_cast<int*>(static_cast<Byte*>(pMem)+realSize-sizeof(int))) 
        = *(static_cast<int*>(pMem)) = signature;

    // 返回真正的内存区域
    return static_cast<Byte*>(pMem) + sizeof(int);
}
```

这个例子主要是展示，它存在很多错误：

1. 所有的operator new都应该内含一个循环，反复调用某个new-handling函数，这里却没有
2. C++要求所有operator new返回的指针都有适当的对齐。这里malloc返回的指针是满足要求的，但是因为上述实现并不是直接返回malloc的结果，而是返回一个int偏移后的地址，因此无法保证它的安全

## 条款51：编写new和delete时需固守常规

前一条款是解释什么时候会想实现自己的 operator new 和 operator delete，这个条款是解释当实现自己的 operator new 和 operator delete 时，必须遵守的规则

### 1）operator new

实现一致性的operator new必得返回正确的值，内存不足时必得调用new-handling函数，必须有对付零内存需求的准备，还需避免不慎掩盖正常形式的new

下面是non-member operator new的伪码：

```c++
void* operator new(std::size_t size) throw(std::bad_alloc)
{
    using namespace std;
    if(size == 0){          //处理0-byte申请
        size = 1;           //将它视为1-byte
    }
    while(true){
        尝试分配size bytes
        if (分配成功)
        return (一个指针，指向分配得来的内存)

        //分配失败：找出目前的new-handling函数
        new_handler globalHandler = set_new_handler(0);
        set_new_handler(globalHandler);
        
        if(globalHandler) (*globalHandler)();
        else throw std::bad_alloc();
    }
}
```

在继承中定制member operator new时，一般是针对某特定class的对象分配行为提供最优化，此时，并不是为了该class的任何derived classes。也就是说，针对class X而设计的operator new，其行为很典型地只为大小刚好为sizeof(X)的对象而设计。然而一旦被继承下去，有可能base class的operator new被调用用以分配derived class对象：

```c++
class Base{
public:
    static void* operator new(std::size_t size) throw(std::bad_alloc);
    ...
};

class Derived : public Base  //假设Derived未声明operator new
{...};

Derived *p = new Derived;    //这里调用的是Base::operator new
```

如果Base class专属的operator new并没有设计上述问题的处理方法，那么最佳做法是将“内存申请量错误”的调用行为改采标准operator new，像这样：

```c++
void* Base::operator new(std::size_t size) throw(std::bad_alloc)
{
    if(Base != sizeof(Base))            //如果大小错误
        return ::operator new(size);    //交给标准的operator new处理
    ...
}
```

### 2）operator delete

operator delete比起operator new更简单，需要记住的唯一事情就是C++保证“删除null指针永远安全”：

```c++
void operator delete(void* rawMemory) throw()
{
    if(rawMemory == 0)  return;  //如果将被删除的是个null指针，那就什么都不做
    现在，归还rawMemory所指的内存；
}
```

member版本也很简单，只需要多一个动作检查删除数量。万一class专属的operator new将大小有误的分配行为转交::operator new执行，你也必须将大小有误的删除行为转交::operator delete执行

```c++
void* Base::operator delete(void* rawMemory,std::size_t size) throw()
{
    if(rawMemory == 0)  return;         //检查null指针
    if(size != sizeof(Base)){           //如果大小错误，令标准版
        ::operator delete(rawMemory);   //operator delete处理此一申请
        return;
    }
    现在，归还rawMemory所指的内存
    return;
}
```

如果即将被删除的对象派生自某个base class，而后者欠缺virtual析构函数，那么C++传给operator delete的size_t数值可能不正确。这是“让你的base classes拥有virtual析构函数”的一个够好的理由

<br>

## 条款52：写了placement new也要写placement delete

placement new是带有额外参数的operator new，但是通常都指“接受一个指针指向对象该被构造之处”的operator new。这个版本被纳入了C++标准程序库，只要#include\<new>\就可以使用：

```c++
void* operator new(std::size_t,void* pMemory) throw();
```

new会先调用operator new，然后构造对象。如果对象构造过程中发生异常，那么需要调用相应的operator delete，否则会发生内存泄露。而operator delete必须和相应的operator new匹配

* 对于正常版本的operator new，匹配的operator delete就是不带额外参数的版本
* 对于非正常版本的operator new(placement new)，匹配的operator delete是带相应参数的版本(placement delete)

placement delete只有在“伴随placement new调用而触发的构造函数”出现异常时才会被调用。对着一个指针施行delete绝不会导致调用placement delete

这意味着如果要对所有与placement new相关的内存泄露宣战，我们必须同时提供一个正常的operator delete（用于构造期间无任何异常被抛出）和一个placement版本（用于构造期间有异常被抛出）。后者的额外参数必须和operator new一样。只要这样做，就再也不会因为难以察觉的内存泄露而失眠

还需要注意名称掩盖的问题：

* 成员函数的名称会掩盖外围作用域中的相同名称
* 子类的名称会掩盖所有父类相同的名称

一个比较好的方法是：

```c++
class StandardNewDeleteForms{
public:
    //正常的 new/delete
    static void* operator new(std::size_t size) throw(std::bad_alloc)
    {return ::operator new(size);}
    static void operator delete(void* pMemory) throw()
    {::operator delete(pMemory);}

    //placement new/delete
    static void* operator new(std::size_t size, void *ptr) throw() 
    { return ::operator new(size, ptr); }
    static void operator delete(void *pMemory, void *ptr) throw() 
    { return ::operator delete(pMemory, ptr); }

    // nothrow new/delete
    static void* operator new(std::size_t size, const std::nothrow_t& nt) throw() 
    { return ::operator new(size, nt); }
    static void operator delete(void *pMemory, const std::nothrow_t&) throw() 
    { ::operator delete(pMemory); }
};

class Widget: public StandardNewDeleteForms {      //继承标准形式
public:
   using StandardNewDeleteForms::operator new;     //让这些形式可见
   using StandardNewDeleteForms::operator delete;

   static void* operator new(std::size_t size, std::ostream& log) throw(std::bad_alloc);    // 自定义 placement new
   static void operator delete(void *pMemory, std::ostream& logStream) throw();            // 对应的 placement delete
};
```




## 9.杂项讨论