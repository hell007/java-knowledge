

## java 反射

#### 定义 

Java反射机制是指在运行状态中，对于任意一个类，都能知道这个类的所有属性和方法，对于任何一个对象，
都能够调用它的任何一个方法和属性，这样动态获取新的以及动态调用对象方法的功能就叫做反射

#### api

Java中有关反射的类有以下这几个：

编译后的class文件的对象

  java.lang.Class

构造方法

  java.lang.reflect.Constructor

类的成员变量（属性）

  java.lang.reflect.Field

类的成员方法

  java.lang.reflect.Method

判断方法类型

  java.lang.reflect.Modifier

类的注解

  java.lang.annotation.Annotation
  
  
#### 实例

  package reflect;
  public class ReflectClass {

    private String name;

    private int age;
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public int getAge() {
      return age;
    }
    public void setAge(int age) {
      this.age = age;
    }

    public void print(){
      System.out.println("name=" + this.name +",age=" + this.age);
    }
    public ReflectClass(String name, int age) {
      super();
      this.name = name;
      this.age = age;
    }
    public ReflectClass() {
      super();
    }
    @Override
    public String toString() {
      return "ReflectClass [name=" + name + ", age=" + age + "]";
    }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + age;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
    }
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ReflectClass other = (ReflectClass) obj;
      if (age != other.age)
        return false;
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      return true;
    }
  }
  
  
##### 获取CLASS 对象

Java 中的所有类型包括基本类型，即使是数组都有与之关联的 Class 类的对象。

1、如果你在编译期知道一个类的名字的话，那么你可以使用如下的方式获取一个类的 Class 对象。

  Class <?>  reflectClass= ReflectClass.class;
  
2、果你已经得到了某个对象，但是你想获取这个对象的 Class 对象，那么你可以通过下面的方法得到:

  ReflectClass reflectClass = new ReflectClass();
  Class <?> reflect=reflectClass.getClass();
  
3、如果你在编译期获取不到目标类型，但是你知道它的完整类路径，那么你可以通过如下的形式来获取 Class 对象:

  Class <?> class1=Class.forName("reflect.ReflectClass");
  
  案列：excel导入
  
在使用 Class.forName()方法时，你必须提供一个类的全名，这个全名包括类所在的包的名字。如果在调用 Class.forName()方法时，
没有在编译路径下(classpath)找到对应的类，那么将会抛出 ClassNotFoundException。

以上三种写法都是相等的

  
##### 获取目标类型的对象

在 java 中要构造对象，必须通过该类的构造函数，那么其实反射也是一样一样的。但是它们确实有区别的，通过反射构造对象，
我们首先要获取类的 Constructor(构造器)对象，然后通过 Constructor 来创建目标类的对象

  public class ReflectTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
      ReflectClass reflectClass = new ReflectClass();
      // 获取Class对象
      Class<?> class1 = reflectClass.getClass();
      Constructor<?> constructor;
      try {
        // ReflectClass有一个含有两个参数的构造函数
        constructor = class1.getConstructor(String.class, int.class);
        try {
          // 创建ReflectClass的对象
          Object object = constructor.newInstance("John", 25);
          System.out.println("obj: " + object.toString());
        } catch (InstantiationException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException e) {
          e.printStackTrace();
        }
      } catch (NoSuchMethodException | SecurityException e1) {
        e1.printStackTrace();
      }
    }
  }
  
  
**注意**
 
当你通过反射获取到 Constructor、Method、Field 后，在反射调用之前将此对象的 accessible 标志设置为 true，以此来提升反射速度。
值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查
  
  Constructor<?> constructor = clz.getConstructor(String.class);
  // 设置 Constructor 的 Accessible
  constructor.setAccessible(true);
  // 设置 Methohd 的 Accessible
  Method learnMethod = Student.class.getMethod("learn"， String.class);
  learnMethod.setAccessible(true);
  
  
 ##### 常用API
 
> 构造函数
 
  // 获取一个公有的构造函数，参数为可变参数，如果构造函数有参数，那么需要将参数的类型传递给 getConstructor 方法
  public Constructor<T> getConstructor (Class...<?> parameterTypes)
  // 获取目标类所有的公有构造函数
  public Constructor[]<?> getConstructors ()
 
> 方法
 
  // 获取 Class 对象中指定函数名和参数的函数，参数一为函数名，参数 2 为参数类型列表
  public Method getDeclaredMethod (String name, Class...<?> parameterTypes)
  // 获取该 Class 对象中的所有函数( 不包含从父类继承的函数 )
  public Method[] getDeclaredMethods ()
  // 获取指定的 Class 对象中的**公有**函数，参数一为函数名，参数 2 为参数类型列表
  public Method getMethod (String name, Class...<?> parameterTypes)
  // 获取该 Class 对象中的所有**公有**函数 ( 包含从父类和接口类集成下来的函数 )
  public Method[] getMethods ()
 
> 属性
 
  // 获取 Class 对象中指定属性名的属性，参数一为属性名
  public Method getDeclaredField (String name)
  // 获取该 Class 对象中的所有属性( 不包含从父类继承的属性 )
  public Method[] getDeclaredFields ()
  // 获取指定的 Class 对象中的**公有**属性，参数一为属性名
  public Method getField (String name)
  // 获取该 Class 对象中的所有**公有**属性 ( 包含从父类和接口类集成下来的公有属性 )
  public Method[] getFields ()
  
> 获取注解
  
  // 获取指定类型的注解
  public <A extends Annotation> A getAnnotation(Class<A> annotationClass) ;
  // 获取 Class 对象中的所有注解
  public Annotation[] getAnnotations() ;
 
 
  [来源](http://crazyandcoder.tech/2016/09/14/java%20%E5%8F%8D%E5%B0%84%E5%AD%A6%E4%B9%A0%E6%80%BB%E7%BB%93/)
  

