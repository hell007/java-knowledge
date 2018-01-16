1.pojo映射实体类

```
@Entity注释指名这是一个实体Bean，@Table注释指定了Entity所要映射带数据库表,@Table.name()用来指定映射表的表名 
@Column注释定义了将成员属性映射到关系表中的哪一列和该列的结构信息,一共有10个属性，这10个属性均为可选属性，各属性含义分别如下：
name属性定义了被标注字段在数据库表中所对应字段的名称；
unique属性表示该字段是否为唯一标识，默认为false。如果表中有一个字段需要唯一标识，则既可以使用该标记，也可以使用@Table标记中的@UniqueConstraint。
nullable属性表示该字段是否可以为null值，默认为true。
insertable属性表示在使用“INSERT”脚本插入数据时，是否需要插入该字段的值。
updatable属性表示在使用“UPDATE”脚本插入数据时，是否需要更新该字段的值。insertable和updatable属性一般多用于只读的属性，例如主键和外键等。这些字段的值通常是自动生成的。
columnDefinition属性表示创建表时，该字段创建的SQL语句，一般用于通过Entity生成表定义时使用。（也就是说，如果DB中表已经建好，该属性没有必要使用。）
table属性定义了包含当前字段的表名。 
length属性表示字段的长度，当字段的类型为varchar时，该属性才有效，默认为255个字符。
precision属性和scale属性表示精度，当字段类型为double时，precision表示数值的总长度，scale表示小数点所占的位数

@Id注释指定表的主键，它可以有多种生成方式： 
TABLE：容器指定用底层的数据表确保唯一； 
SEQUENCE：使用数据库的SEQUENCE列来保证唯一（Oracle数据库通过序列来生成唯一ID）； 
IDENTITY：使用数据库的IDENTITY列来保证唯一； 
AUTO：由容器挑选一个合适的方式来保证唯一； 
NONE：容器不负责主键的生成，由程序来完成。 
@GeneratedValue注释定义了标识字段生成方式。 
@Temporal注释用来指定java.util.Date或java.util.Calender属性与数据库类型date、time或timestamp中的那一种类型进行映射。 
@Temporal(value=TemporalType.TIME)

```

2. #ORM 框架 Hibernate  #接口 JpaRepository


3.thymeleaf

[thymeleaf使用](http://blog.csdn.net/u012706811/article/details/52185345)

