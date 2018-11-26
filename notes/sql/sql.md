
## 创建表



>MYSQL

	CREATE TABLE T_PERSON(
		FID INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'FID',
		FNAME VARCHAR(20) NOT NULL UNIQUE,
		FAGE INT UNSIGNED,
		FREMARK VARCHAR(20)
	)
	
	CREATE TABLE T_DEBT(
		FNUMBER VARCHAR(20) NOT NULL PRIMARY KEY,
		FAMOUNT DECIMAL(10,2) NOT NULL,
		FPERSON VARCHAR(20),
		FOREIGN KEY(FREMARK) REFERENCES T_PERSON(FNAME)
	)



>Oracle

	CREATE TABLE T_PERSON(
		FNAME VARCHAR2(20) NOT NULL PRIMARY KEY,
		FAGE NUMBER(10),
		FREMARK VARCHAR2(20)
	)
	
	CREATE TABLE T_DEBT(
		FNUMBER VARCHAR2(20) NOT NULL PRIMARY KEY,
		FAMOUNT NUMERIC(10,2) NOT NULL,
		FPERSON VARCHAR2(20),
		FOREIGN KEY(FPERSON) REFERENCES T_PERSON(FNAME)
	)

### 修改mysql 表的存储引擎

如何将MyISAM库导成INNODB引擎格式的:

1.在备份出的xxx.sql文件中把ENGINE=MyISAM全换成ENGINE=INNODB再次导入就可以了

2.转换表的命令:

```
mysql> alter table 表名 engine = innodb;
```


## 表复制

1.复制源表的结构并复制表中的数据

在MYSQL和ORACLE中支持从结果集来创建一张表，并且将结果集中的数据填充到新创建的表中

	CREATE TABLE T_Person2
	AS
	SELECT * FROM T_Person //复制T_Person表结构和数据

2.只复制源表的结构

制源表的结构，只需要让结果集为空就可以了

>注意结果集为空并不表示结果集没有意义，因为空的结果集中仍然包含了结果集的列信息

MYSQL和ORACLE：

创建一个空结果集的最简单方式就是使用永远为FALSE的WHERE条件“WHERE 1<>1”

	CREATE TABLE T_Person2
	AS
	SELECT * FROM T_Person
	WHERE 1<>1  //复制T_Person表结构

“WHERE 1<>1”这样的查询条件有可能造成全表扫描从而带来性能问题，因此如果数据量比较大的话可以用取结果集前0条这样的方式来得到一个空结果集

	CREATE TABLE T_Person2
	AS
	SELECT * FROM T_Person
	LIMIT 0,0  //复制T_Person表结构  (???)


## ADD FEILD

	ALTER TABLE T_EMP ADD FAGE INT
	
	ALTER TABLE T_EMP ADD FSEX CHAR(2)

## ALTER FEILD

>MYSQL

1.修改字段类型

ALTER TABLE 表名  MODIFY COLUMN 字段名 字段类型定义

	ALTER TABLE T_PERSON MODIFY COLUMN FNAME VARCHAR(20) NOT NULL DEFALUT NULL

2.修改主键

	ALTER TABLE T_PERSON CHANGE FID FID INT AUTO_INCREMENT
	
可在建表时可用“AUTO_INCREMENT=n”选项来指定一个自增的初始值

	ALTER TABLE T_PERSON  AUTO_INCREMENT = X //1000

## DROP FEILD

	ALTER TABLE T_EMP DROP FAGE
	
	ALTER TABLE T_EMP DROP FSEX


## DROP TABLE

	DROP TABLE T_EMP


## INSERT

	INSERT INTO T_PERSON (FNAME,FAGE,FREMARK) VALUES ('KD',28,'USA')

	INSERT INTO T_DEBT (FNUMBER,FAMOUNMT,FPERSON,FPERSON) VALUES ('1000',2000.00,'KD')


## UPDATE

	UPDATE T_PERSON SET FREMARK = 'SUPERMAN'

	UPDATE T_PERSON SET FAGE = 22, FNAME = 'TOM' WHRER ...


## SELECT

	SELECT FNUMBER AS NUMBER,FNAME AS NAME,FAGE AS AGE,FSALARY AS SALARY FROM T_EMP 

	SELECT FNUMBER NUMBER,FNAME NAME,FAGE AGE,FSALARY SALARY FORM T_EMP


## MAX

	SELECT MAX(FSALARY) FROM T_EMP WHERE FAGE > 25

	SELECT MAX(FSALARY) AS MAX_SALARY FROM T_EMP WHERE FAGE >25


## AVG

	SELECT AVG(FAGE) FROM T_EMP WHERE FSALARY > 3800


## SUM

	SELECT SUM(FSALARY) FORM T_EMP


## MIN MAX

	SELECT MIN(FSALARY),MAX(FSALARY) FROM T_EMP

去除所有最低最高值,得到平均值

mysql

	SELECT AVG(FCount) FROM T_SaleBillDetail
	WHERE FCount NOT IN
	(
	(SELECT MIN(FCount) FROM T_SaleBillDetail),
	(SELECT MAX(FCount) FROM T_SaleBillDetail)
	)

oracle  窗口函数来完成

	SELECT AVG(t.FCount)
	FROM
	(
	SELECT FCount,MIN(FCount) OVER() min_count,
	MAX(FCount) OVER() max_count  //窗口函数
	FROM T_SaleBillDetail
	)t
	WHERE t.FCount NOT IN(min_count, max_count)

只去除一个最低最高值

	SELECT
	CASE COUNT(FCount)
	WHEN 2 THEN NULL //只有两条数据的bug处理
	ELSE (SUM(FCount)-
	END
	FROM T_SaleBillDetail

## COUNT

	SELECT COUNT(*),COUNT(FNUMBER) FROM T_EMP
	
	COUNT(FNAME) FNAME === NULL 条数不计人


## ORDER BY

	SELECT * FROM T_EMP ORDER BY FAGE ASC //升
	
	== SELECT * FROM T_EMP ORDER BY FAGE (可省略)
	
	SELECT * FROM T_EMP ORDER BY FAGE DESC //降
	
	
	SELECT * FROM T_EMP ORDER BY FAGE DESC,FSALARY DESC //多条件


## 通配符过滤 LIKE

1、单字符匹配 "_"

	SELECT * FROM T_EMP WHERE FNAME LIKE '_erry' //(匹配第一个字符为any的 erry )
	
	//Jerry 
	//Kerry

2、多字符匹配 "%"

	"b%"   b开头
	"%t"   t结尾
	"b%t"  b开头t结尾
	"%n%"  含有n
	"%n_"  最后一个字符为任意字符、倒数第二个字符为“n”、长度任意的字符串

3、集合匹配 "[]" (集合匹配只在 MSSQLServer 上提供支持，在 MYSQL、Oracle、DB2 等数据库中不支持)

	"[bt]%"  匹配第一个字符为 b 或者 t 长度不限的字符串
	
	"[^bt]%" 匹配第一个字符不为 b 或者 t、长度不限的字符串


MYSQL、Oracle、DB2 变向实现集合匹配

	"[bt]%"  ===  SELECT * FROM T_EMP WHERE FNAME LIKE 'b%' OR FNAME LIKE 't%'
	
	"[^bt]%" ===  SELECT * FROM T_EMP WHERE NOT(FNAME LIKE 'b%') AND NOT(FNAME LIKE 't%')

非常强大的功能，不过在使用通配符过滤进行检索的时候，数据库系统
会对全表进行扫描，所以执行速度非常慢,慎用


## 空值检测  

	SELECT * FROM T_EMP WHERE FNAME IS NULL
	
	SELECT * FROM T_EMP WHERE FNAME IS NOT NULL
	
	SELECT * FROM T_EMP WHERE FNAME IS NOT NULL AND FSALARY < 5000


## 反义运算符

!>   !<  ...

mysql 
 
 < 、 > 、 <> => (!=)

	SELECT * FROM T_EMP WHERE FAGE <> 22 AND FSALARY >= 2000


## 多值检测 IN

	SELECT FAGE,FNUMBER,FNAME FROM T_EMP WHERE FAGE=23 OR FAGE = 25

IN

	SELECT FAGE,FNUMBER,FNAME FROM T_EMP WHERE FAGE IN (23,25)


## 范围值检测  BETWEEN ... AND ...

IN 

	WHERE FAGE>=23 AND FAGE <=27
	
	SELECT * FROM T_EMP WHERE FAGE BETWEEN 23 AND 27


## 低效的 "WHERE 1=1"

	SELECT * FROM T_EMP WHERE 1=1 
	AND FNUMBER BETWEEN 'DEV001' AND 'DEV008'
	AND FSALARY BETWEEN 3000 AND 6000

注意点：因为使用添加了“1=1”的过滤条件以后数据库系统就无法使用索引等查询优化策略，
数据库系统将会被迫对每行数据进行扫描（也就是全表扫描）以比较此行是否满足过滤条件，
当表中数据量比较大的时候查询速度会非常慢


## 数据分组 GROUP BY （统计field /统计列）


统计每个分公司的年龄段的人数

	SELECT FSUBCOMPANY,FAGE,COUNT(*) AS PCOUNTS FROM T_EMP
	GROUP BY FSUBCOMPANY,FAGE

	SELECT FSUBCOMPANY,FAGE,COUNT(*) AS PCOUNTS FROM T_EMP
	GROUP BY FSUBXOMPANY,FAGE
	ORDER BY FSUBCOMPANY

统计每个公司中的工资的总值

	SELECT FSUBCOMPANY,SUM(FSALARY) AS FSALARYSUM FROM T_EMP
	GROUP BY FSUBCOMPANY

统计每个垂直部门中的工资的平均值

	SELECT FDEPARTMENT,AVG(FSALARY) AS FSALARYAVG FROM T_EMP
	GROUP BY FDEPARTMENT

统计每个垂直部门中员工年龄的最大值和最小值

	SELECT FDEPARTMENT,MAX(FAGE) AS FAGEMAX,MIN(FAGE) AS FAGEMIN FROM T_EMP
	GROUP BY FDEPARTMENT


##  HAVING 

	SELECT FAge,COUNT(*) AS CountOfThisAge FROM T_Employee
	GROUP BY FAge
	WHERE COUNT(*)>1 //报错

	SELECT FAGE,COUNT(*) AS FACOUNTS FROM T_EMP
	GROUP BY FAGE
	HAVING COUNT(*) =1 


	SELECT FAGE,COUNT(*) AS FACOUNTS FROM T_EMP
	GROUP BY FAGE
	HAVING COUNT(*) =1 OR COUNT(*) =3

	SELECT FAGE,COUNT(*) AS FACOUNTS FROM T_EMP
	GROUP BY FAGE 
	HAVING COUNT(*) IN (1,3)

	SELECT FAGE,COUNT(*) AS FACOUNTS FROM T_EMP
	GROUP BY FAGE  //通过FAGE分组
	HAVING FNAME IS NOT NULL //报错  注意：HAVING语句中不能包含未分组的列名

	SELECT FAGE,COUNT(*) AS FCOUNTS FROM T_EMP
	WHERE FNAME IS NOT NULL  //未分组的列名使用WHERE
	GROUP BY FAGE

HAVING  VS  WHERE

是否使用未分组的列名（ group by 产生的结果集的列 ）


## 限制结果集行数 

MYSQL  LIMIT

	SELECT * FROM T_EMP ORDER BY FSALARY DESC LIMIT 2,5

ORACLE  rownum  （这里需要学习下oracle）

	SELECT * FROM T_EMP 
	WHERE rownum <=6 
	ORDER BY FSALARY DESC

	SELECT rownum,FNumber,FName,FSalary,FAge FROM T_Employee
	WHERE rownum BETWEEN 3 AND 5
	ORDER BY FSalary DESC //报错


实现数据库分页


## 抑制数据重复

注意：DISTINCT关键字是用来进行重复数据抑制的最简单的功能，而且所有的数据库系统都支持DISTINCT

	SELECT DISTINCT FDEPARTMENT FROM T_EMP

注意：DISTINCT是对整个结果集进行数据重复抑制的，而不是针对每一个列，执行下面的SQL语句

	SELECT DISTINCT FDEPARTMENT,FSUBCOMPANY FROM T_EMP


##  计算字段

1.常量字段

	SELECT 'CowNew集团',918000000,FNAME,FAGE,FSUBCOMPANY FROM T_EMP

'CowNew集团'和918000000并不是一个实际的存在的列，但是在查询出来的数据
中它们看起来是一个实际存在的字段，这样的字段被称为“常量字段”（也称为“常量值”

	SELECT 'CowNew 集 团' AS COMPANYNAME, 918000000 AS REGAMOUNT,FNAME,FSUBCOMPANY FROM T_EMP

2.字段间计算

	SELECT FNUMBER,FNAME,FAGE * FSALARY AS FSALARYINDEX FROM T_EMP
	
	SELECT 125+521,FNUMBER,FNAME,FSALARY/(FAGE-21) AS FHAPPYINDEX FROM T_EMP
	
	SELECT * FROM T_EMP WHERE FSALARY/(FAGE-21) > 1000


## 数据处理函数 参看函数第五章

#### 数学函数

1.求绝对值   ABS()函数用来返回一个数值的绝对值。该函数接受一个参数

	SELECT FWeight - 50,ABS(FWeight - 50) , ABS(-5.38) FROM T_Person

2.求指数  POWER()函数是用来计算指数的函数。该函数接受两个参数，第一个参数为待求幂的表达式，第二个参数为幂

	SELECT FWeight,POWER(FWeight,-0.5),POWER(FWeight,2),POWER(FWeight,3),POWER(FWeight,4) FROM T_Person

求幂  POWER(X,Y)函数用来计算 X 的 Y 次幂

	SELECT FName,FWeight, POWER(1.18,FWeight) FROM T_Person

3.求平方根  SQRT()函数是用来计算平方根的函数。该函数接受一个参数

	SELECT FWeight,SQRT(FWeight) FROM T_Person

4.求随机数 

mysql   RAND()函数用来生成随机算法    ( RAND()函数的返回值是随机的)

	SELECT RAND()

Oracle 中没有内置的生成随机数的函数，不过 Oracle 提供了包 dbms_random 用来生成随机数

dbms_random.value(low, high)用来返回一个大于或等于 low，小于 high 的随机数

	SELECT dbms_random.value FROM dual
	
	SELECT dbms_random.value(60,100) FROM dual

dbms_random.normal 用来返回服从正态分布的一组数

dbms_random.string(opt, len)用来返回一个随机字符串，opt 为选项参数，len 表示返回的字符串长度，最大值为 60

参数 opt 可选值如下：

'U'：返回全是大写的字符串

'L'：返回全是小写的字符串

'A'：返回大小写结合的字符串

'X'：返回全是大写和数字的字符串

'P'：返回键盘上出现字符的随机组合

5.舍入到最大整数

在 MYSQL、MSSQLServer 和 DB2 中提供了名称为 CEILING()函数，Oracle 也提供了类似的函数 CEIL()
这个函数用来舍掉一个数的小数点后的部分，并且向上舍入到邻近的最大的整数

-3.61 将被舍入为-3 (最大整数)

	SELECT FName,FWeight, CEILING(FWeight), CEILING(FWeight*-1) FROM T_Person

	SELECT FName,FWeight, CEIL(FWeight) , CEIL (FWeight*-1) FROM T_Person

6.舍入到最小整数

FLOOR()函数用来舍掉一个数的小数点后的部分，并且向下舍入到邻近的最小的整数

	SELECT FName,FWeight,FLOOR(FWeight),FLOOR(FWeight*-1) FROM T_Person

7.四舍五入  ROUND()函数也是用来进行数值四舍五入的

ROUND()函数有两个参数和单一参数两种用法

两个参数  

ROUND(m,d)，其中 m 为待进行四舍五入的数值，而 d 则为计算精度，也就是进行四舍五入时保留的小数位数

233.7 进行精度为-2 的四舍五入得到 200

	SELECT FName,FWeight, ROUND(FWeight,1),
	ROUND(FWeight*-1,0) , ROUND(FWeight,-1)
	FROM T_Person

单一参数

ROUND(m)，其中 m 为待进行四舍五入的数值，它可以看做精度为 0 的四舍五入运算，也就是 ROUND(m,0)

	SELECT FName,FWeight, ROUND(FWeight), ROUND(FWeight*-1)
	FROM T_Person

8.求正弦值  SIN()，它接受一个参数

	SIN(FWeight)

求反正弦值  ASIN()，它接受一个参数

9.求余弦值  COS()，它接受一个参数

求反余弦值   ACOS()，它接受一个参数


10.求正切值  TAN()，它接受一个参数

求反正切值  ATAN()，它接受一个参数

11.求余切  COT()，它接受一个参数

在 Oracle 中不支持这个函数，不过根据余切是正切的倒数这一个特性，可以使用 TAN()函数来变通实现

	SELECT FName,FWeight,1/tan(FWeight) FROM T_Person

12.求圆周率π值

在 MYSQL 和 MSSQLServer 中提供了 PI()函数用来取得圆周率π值，这个函数不需要使用参数

	SELECT FName,FWeight,FWeight *PI() FROM T_Person

在 Oracle 和 DB2 中不支持 PI()函数，不过根据-1 的反余弦值等于π值的这一特性，我们可以用 ACOS(-1)来变通实现

	SELECT FName,FWeight,FWeight * acos(-1) FROM T_Person

13.弧度制转换为角度制

用来将一个数值从弧度制转换为角度制的函数为 DEGREES ()，它接受一个参数

MYSQL,MSSQLServe

	SELECT FName,FWeight, DEGREES(FWeight) FROM T_Person

在 Oracle 和 DB2 中不支持这个函数，不过根据：
角度制=弧度制*180/π
这一个特性，可以用变通方式来实现

	SELECT FName,FWeight,(FWeight*180)/acos(-1) FROM T_Person

14.角度制转换为弧度制

用来将一个数值从角度制转换为弧度制的函数为 RADIANS ()，它接受一个参数

MYSQL,MSSQLServe

	SELECT FName,FWeight, RADIANS(FWeight) FROM T_Person

在 Oracle 和 DB2 中不支持这个函数，不过根据：
弧度制=角度制*π/180
这一个特性，可以用变通方式来实现

	SELECT FName,FWeight,(FWeight*acos(-1)/180) FROM T_Person


15.求符号  

SIGN()函数用来返回一个数值的符号，如果数值大于 0 则返回 1，如果数值等于 0 则返
回 0，如果数值小于 0 则返回-1。该函数接受一个参数

	SELECT FName,FWeight-48.68,SIGN(FWeight-48.68) FROM T_Person

16.求整除余数

MOD()函数用来计算两个数整除后的余数。该函数接受两个参数，第一个参数为除数，而第二个参数则是被除数

MYSQL,Oracle

	SELECT FName,FWeight,MOD(FWeight , 5) FROM T_Person

17.求自然对数  LOG ()函数用来计算一个数的自然对数值。该函数接受一个参数;在 Oracle 中这个函数的名称为 LN()

MYSQL,MSSQLServer,DB2

	SELECT FName,FWeight, LOG(FWeight) FROM T_Person

Oracle

	SELECT FName,FWeight, LN(FWeight) FROM T_Person

18. 求以 10 为底的对数

LOG10()函数用来计算一个数的以 10 为底的对数值。该函数接受一个参数

MYSQL,MSSQLServer,DB2

	SELECT FName,FWeight, LOG10(FWeight) FROM T_Person


在 Oracle 中不支持这个函数，不过 Oracle 中有一个可以计算任意数
为底的对数的函数 LOG(m,n)，它用来计算以 m 为底 n 的对数，我们将 m 设为常量 10 就可以了

Oracle
	
	SELECT FName,FWeight,LOG(10,FWeight) FROM T_Person



#### 字符串函数

1.计算字符串长度的函数

MYSQL、Oracle、DB2中这个函数名称为LENGTH;   MSSQLServer中 LEN

	SELECT FNAME,LENGTH(FNAME) AS NAMELENGTH FROM T_EMP WHERE FNAME IS NOT NULL

2.字符串转换为小写

LOWER()函数用来将一个字符串转换为小写。该函数接受一个参数，此参数为待转换的字符串表达式，在 DB2 中这个函数名称为 LCASE()

	SELECT FName, LOWER(FName) FROM T_Person

3.字符串转换为大写

UPPER ()函数用来将一个字符串转换为大写。该函数接受一个参数,在 DB2 中这个函数名称为 UCASE ()

	SELECT FName, UPPER(FName) FROM T_Person

4.截去字符串空格

LTRIM()函数用来将一个字符串左侧的空格去掉。该函数接受一个参数

	SELECT FName,LTRIM(FName),LTRIM(' abc ') FROM T_Person

RTRIM()函数用来将一个字符串左侧的空格去掉。该函数接受一个参数

	SELECT FName,RTRIM(FName),RTRIM(' abc ') FROM T_Person

TRIM()函数用来将一个字符串两侧的空格去掉。该函数接受一个参数 

此函数只在 MYSQL 和 Oracle 中提供支持，不过在 MSSQLServer 和
DB2 中可以使用 LTRIM ()函数和 RTRIM ()函数复合来进行变通实现，也就是用
LTRIM(RTRIM(string))来模拟实现 TRIM (string)

MYSQL,Oracle

	SELECT FName,TRIM(FName),TRIM(' abc ') FROM T_Person

MSSQLServer,DB2

	SELECT FName,LTRIM(RTRIM(FName)),LTRIM(RTRIM(' abc ')) FROM T_Person

5.取得字符串的子串的函数

接受三个参数，
第一个参数为要取的主字符串，
第二个参数为字串的起始位置（从1开始计数），
第三个参数为字串的长度

>MYSQL、MSSQLServer中这个函数名称为SUBSTRING

	SELECT FNAME,SUBSTRING(FNAME,2,3) FROM T_EMP WHERE FNAME IS NOT NULL

>Oracle、DB2这个函数名称为SUBSTR

	SELECT FNAME,SUBSTR(FNMAE,2,3)FROM T_EMP WHERE FNAME IS NOT NULL

6.计算子字符串的位置 

INSTR(string,substring)

其中参数 string 为主字符串，参数 substring 为待查询的子字符串。如果 string 中存在
substring 子字符串，则返回子字符串第一个字符在主字符串中出现的位置；如果 string 中不
存在 substring 子字符串，则返回 0

MYSQL,Oracle

	SELECT FName, INSTR(FName,'m') , INSTR(FName,'ly') FROM T_Person

7.从左\右侧开始取子字符串

LEFT (string,length) 其中参数 string 为主字符串，length 为子字符串的最大长

RIGHT (string,length)其中参数 string 为主字符串，length 为子字符串的最大长度

MYSQL、MSSQLServer、DB2 

	SELECT FName, LEFT(FName,3) , LEFT(FName,2) FROM T_Person

	SELECT FName, RIGHT(FName,3) , RIGHT(FName,2) FROM T_Person

Oracle 中不支持 LEFT()函数，只能使用 SUBSTR()函数进行变通实现，也就是SUBSTR(string, 1, length)

	SELECT FName,SUBSTR(FName, 1,3),SUBSTR(FName, 1,2) FROM T_Person //左

	SELECT FName,
	SUBSTR(FName, LENGTH(FName)-3 +1, 3),//右
	SUBSTR(FName, LENGTH(FName)-2 +1, 2) FROM T_Person

注意：使用 SUBSTRING()函数我们可以从任意位置开始取任意长度的子字符串

8.字符串替换

REPLACE()函数可以用来将字符串的指定的子字符串替换为其它的字符串

REPLACE(string,string_tobe_replace,string_to_replace)
其中参数 string 为要进行替换操作的主字符串，参数 string_tobe_rep
字符串，而 string_to_replace 将替换 string_tobe_replace 中所有出现的地方

	select FName,
	REPLACE(FName,'i','e'),
	FIDNumber,
	REPLACE(FIDNumber,'2345','abcd') FROM T_Person

SQL 中没有提供删除字符串中匹配的子字符串的方法，因为使用 REPLACE()函数就可
以达到删除子字符串的方法，那就是将第三个参数设定为空字符串

	SELECT FName, REPLACE(FName,'m','') ,//删除
	FIDNumber,
	REPLACE(FIDNumber,'123','') FROM T_Person//删除

9.得到字符的 ASCII码

ASCII()函数用来得到一个字符的 ASCII 码，它有且只有一个参数，这个参数为待求
ASCII 码的字符，如果参数为一个字符串则函数返回第一个字符的 ASCII 码

	SELECT ASCII('a') , ASCII('abc')

	SELECT ASCII('a') , ASCII('abc') FROM DUAL

10.得到一个 ASCII码数字对应的字符

MYSQL、MSSQLServer 和 DB2 中，这个函数的名字是 CHAR()，而在 Oracle 中这个函数的名字则为 CHR()

	SELECT CHAR(56) , CHAR(90) ,'a', CHAR( ASCII('a') )

	SELECT CHR(56) , CHR(90) ,'a', CHR( ASCII('a') ) FROM DUAL

11.发音匹配度

SQL 中提供了 SOUNDEX()函数用于计算一个字符串的发音特征值，这个特征值为一个
四个字符的字符串，特征值的第一个字符总是初始字符串中的第一个字符，而其后则是一个
三位数字的数值

	SELECT SOUNDEX('jack') , SOUNDEX('jeck') , SOUNDEX('joke') ,
	SOUNDEX('juke') , SOUNDEX('look') , SOUNDEX('jobe')

	SELECT SOUNDEX('jack') , SOUNDEX('jeck') , SOUNDEX('joke') ,
	SOUNDEX('juke') , SOUNDEX('look') , SOUNDEX('jobe')
	FROM DUAL

12.替换指定字符串

	INSERT(str,pos,len,newstr)

返回字符串str，使用在开始位置pos和len个字符的的字符串，newstr取代长字符串。返回原始字符串，如果帖子不是字符串的长度范围内。从替换位置pos字符串的其余部分如果len不是字符串的其余部分的长度范围内。返回NULL，如果任何参数是NULL。

查询手机号码，中间四位为 * 替换
	
	SELECT FNAME,INSERT(FPHONE,4,4,'****') FROM T_PERSON

#### 日期时间函数

1.主流数据库系统中日期时间类型的表示方式

在 MYSQL、MSSQLServer 和 DB2 中可以用字符串来表示日期时间类型，数据库系统会自动在内部将它们转换为日期时间类型

在 Oracle 中以字符串表示的数据是不能自动转换为日期时间类型的，必须使用TO_DATE()函数来手动将字符串转换为日期时间类型

2.取得当前日期时间

>MYSQL

NOW()函数用于取得当前的日期时间，NOW()函数还有 SYSDATE()、CURRENT_TIMESTAMP 等别名

	SELECT NOW(),SYSDATE(),CURRENT_TIMESTAMP

如果想得到不包括时间部分的当前日期，则可以使用 CURDATE()函数，CURDATE()函数还有 CURRENT_DATE 等别名

	SELECT CURDATE(),CURRENT_DATE

如果想得到不包括日期部分的当前时间，则可以使用 CURTIME()函数，CURTIME ()函数还有 CURRENT_TIME 等别名

	SELECT CURTIME(),CURRENT_TIME

>Oracle

Oracle 中没有提供取得当前日期时间的函数，不过我们可以到系统表 DUAL 中查询SYSTIMESTAMP 的值来得到当前的时间戳

	SELECT SYSTIMESTAMP FROM DUAL  //当前的时间戳

	SELECT SYSDATE FROM DUA  //当前日期时间

Oracle 中也没有专门提供取得当前日期、取得当前时间的函数，不过我们可以将SYSDATE 的值进行处理，这里需要借助于 TO_CHAR()函数

	SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD') FROM DUAL

	SELECT TO_CHAR(SYSDATE, 'HH24:MI:SS') FROM DUAL

3.日期增减

> MYSQL

MYSQL中提供了DATE_ADD()函数用于进行日期时间的加法运算，这个函数还有一个
别名为ADDDATE()，DATE_ADD()函数的参数格式如下：

	DATE_ADD (date,INTERVAL expr type)

其中参数date为待计算的日期；参数expr为待进行加法运算的增量，它可以是数值类型
或者字符串类型，取决于type参数的取值；参数type则为进行加法运算的单位，type参数可
选值以及对应的expr参数的格式如下表  参看 5.3.4.1

	SELECT FBirthDay,
	DATE_ADD(FBirthDay,INTERVAL 1 WEEK) as w1,
	DATE_ADD(FBirthDay,INTERVAL 2 MONTH) as m2,
	DATE_ADD(FBirthDay,INTERVAL 5 QUARTER) as q5
	FROM T_Person

	SELECT FBirthDay,
	DATE_ADD(FBirthDay,INTERVAL '3 2:10' DAY_MINUTE) as dm,
	DATE_ADD(FBirthDay,INTERVAL '1-6' YEAR_MONTH) as ym
	FROM T_Person

DATE_ADD()函数不仅可以用来在日期基础上增加指定的时间段，而且还可以在日期基础
上减少指定的时间段，只要在expr参数中使用负数就可以

	SELECT FBirthDay,
	DATE_ADD(FBirthDay,INTERVAL -1 WEEK) as w1,
	DATE_ADD(FBirthDay,INTERVAL -2 MONTH) as m2,
	DATE_ADD(FBirthDay,INTERVAL -5 QUARTER) as q5
	FROM T_Person


在MYSQL中提供了DATE_SUB()函数用于计算指定日期前的特定时间段的日
期，其效果和在DATE_ADD()函数中使用负数的expr参数值的效果一样，其用法也和
DATE_ADD()函数几乎相同

	SELECT FBirthDay,
	DATE_SUB(FBirthDay,INTERVAL 1 WEEK) as w1,
	DATE_SUB(FBirthDay,INTERVAL 2 MONTH) as m2,
	DATE_SUB(FBirthDay, INTERVAL '3 2:10' DAY_MINUTE) as dm
	FROM T_Person


>Oracle

Oracle中可以直接使用加号“+”来进行日期的加法运算，其计算单位为“天”，比如date+3
就表示在日期date的基础上增加三天；同理使用减号“-”则可以用来计算日期前的特定时间
段的时间

	SELECT FBirthDay,
	FBirthDay+3,
	FBirthDay-10
	FROM T_Person

可以使用换算的方式来进行以周、小时、分钟等为单位的日期加减运算

	每个人出生日期2小时10分钟后以及3周后的日期
	SELECT FBirthDay,FBirthDay+(2/24+10/60/24),
	FBirthDay+(3*7)
	FROM T_Person

使用加减运算我们可以很容易的实现以周、天、小时、分钟、秒等为单位的日期的增减运
算，不过由于每个月的天数是不同的，也就是在天和月之间不存在固定的换算率，所以无法使用
加减运算实现以月为单位的计算，为此Oracle中提供了ADD_MONTHS()函数用于以月为单位的
日期增减运算，ADD_MONTHS()函数的参数格式如下：

	ADD_MONTHS(date,number)

其中参数date为待计算的日期，参数number为要增加的月份数，如果number为负数则表
示进行日期的减运算

	SELECT FBirthDay,
	ADD_MONTHS(FBirthDay,2),
	ADD_MONTHS(FBirthDay,-10)
	FROM T_Person

	计算每个人的出生日期两个月零10天后以及3个月零10个小时前的日期时间
	SELECT FBirthDay,
	ADD_MONTHS(FBirthDay,2)+10 as bfd,
	ADD_MONTHS(FBirthDay,-3)-(10/24) as afd
	FROM T_Person

4.计算日期差额

>MYSQL

MYSQL中使用DATEDIFF()函数用于计算两个日期之间的差额，其参数调用格式如下：

	DATEDIFF(date1,date2)

函数将返回date1与date2之间的天数差额，如果date2在date1之后返回正值，否则返回负值

	SELECT FRegDay,FBirthDay, 
	DATEDIFF(FRegDay, FBirthDay),
	DATEDIFF(FBirthDay ,FRegDay)
	FROM T_Person

DATEDIFF()函数只能计算两个日期之间的天数差额，如果要计算两个日期的周差额等
就需要进行换算

	SELECT FRegDay,FBirthDay, 
	DATEDIFF(FRegDay, FBirthDay)/7
	FROM T_Person

>Oracle

在Oracle中，可以在两个日期类型的数据之间使用减号运算符“-”，其计算结果为两个日期之间的天数差

	SELECT FRegDay,FBirthDay,FRegDay-FBirthDay FROM T_Person

注意通过减号运算符“-”计算的两个日期之间的天数差是包含有小数部分的，小数部分表示不足一天的部分

	SELECT SYSDATE,FBirthDay,SYSDATE-FBirthDay FROM T_Person

可以看到天数差的小数部分是非常精确的，所以完全可以精确的表示两个日期时间值之
间差的小时、分、秒甚至毫秒部分。所以如果要计算两个日期时间值之间的小时、分、秒以
及毫秒差的话，只要进行相应的换算就可以

	SELECT (SYSDATE-FBirthDay)*24,
	(SYSDATE-FBirthDay)*24*60,
	(SYSDATE-FBirthDay)*24*60*60
	FROM T_Person

	SELECT SYSDATE,FBirthDay,(SYSDATE-FBirthDay)/7 FROM T_Person

可以看到计算结果含有非常精确的小数部分，不过如果对这些小数部分没有需求的话则
可以使用数值函数进行四舍五入、取最大整数等处理

	SELECT
	ROUND((SYSDATE-FBirthDay)*24),
	ROUND((SYSDATE-FBirthDay)*24*60),
	ROUND((SYSDATE-FBirthDay)*24*60*60)FROM T_Person

5.计算一个日期是星期几

>MYSQL

MYSQL中提供了DAYNAME()函数用于计算一个日期是星期几

	SELECT FBirthDay,
	DAYNAME(FBirthDay),
	FRegDay,
	DAYNAME(FRegDay)
	FROM T_Person

*注意MYSQL中DAYNAME()函数返回的是英文的日期表示法*

>Oracle

Oracle中提供了TO_CHAR()函数用于将数据转换为字符串类型，当针对时间日期类型数
据进行转换的时候，它接受两个参数，其参数格式如下：
	TO_CHAR(date,format)
其中参数date为待转换的日期，参数format为格式化字符串  详细5.3.6.3 Oracle

	SELECT FBirthDay,
	TO_CHAR(FBirthDay, 'YYYY') as yyyy,
	TO_CHAR(FBirthDay, 'MM') as mm,
	TO_CHAR(FBirthDay, 'MON') as mon,
	TO_CHAR(FBirthDay, 'WW') as ww
	FROM T_Person

	SELECT FBirthDay,
	TO_CHAR(FBirthDay, 'YYYY-MM-DD') as yyymmdd,
	FRegDay,
	TO_CHAR(FRegDay, 'DD-YYYY-MM') as ddyyyymm
	FROM T_Person

当用“DAY”做为参数的时候就可以将日期格式化为名字的形式表示的星期几

	SELECT
	FBirthDay,
	TO_CHAR(FBirthDay, 'DAY') as birthwk,
	FRegDay,
	TO_CHAR(FRegDay, 'DAY') as regwk
	FROM T_Person



6.DATE_FORMAT()函数

MYSQL中提供了一个DATE_FORMAT()函数用来将日期按照特定各是进行格式化，这
个函数的参数格式如下：
	DATE_FORMAT(date,format)
这个函数用来按照特定的格式化指定的日期，其中参数date为待计算的日期值，而参数format为格式化字符串

	SELECT
	FBirthDay,
	DATE_FORMAT(FBirthDay,'%y-%M %D %W') AS bd,
	FRegDay,
	DATE_FORMAT(FRegDay,'%Y年%m月%e日') AS rd
	FROM T_Person

	SELECT
	FBirthDay,
	DATE_FORMAT(FBirthDay,'%Y') AS y,
	DATE_FORMAT(FBirthDay,'%j') AS d,
	DATE_FORMAT(FBirthDay,'%U') AS u
	FROM T_Person


#### 其他函数

1.COALESCE()函数

主流数据库系统都支持COALESCE()函数，这个函数主要用来进行空值处理，其参数格
式如下：
	COALESCE ( expression,value1,value2……,valuen)
COALESCE()函数的第一个参数expression为待检测的表达式，而其后的参数个数不定

如果expression不为空值则返回expression；否则判断value1是否是空值，如果value1不为空值则返
回value1；否则判断value2是否是空值，如果value2不为空值则返回value3；……以此类推，
如果所有的表达式都为空值，则返回NULL

**注意：sql里面 '' 不是空, null才是 **

MYSQL、MSSQLServer、DB2

	SELECT FName,FBirthDay,FRegDay,
	COALESCE(FBirthDay,FRegDay,'2008-08-08') AS ImportDay
	FROM T_Person

Oracle:

	SELECT FBirthDay,FRegDay,
	COALESCE(FBirthDay,FRegDay,TO_DATE('2008-08-08', 'YYYY-MM-DD HH24:MI:SS'))
	AS ImportDay
	FROM T_Person

COALESCE()函数的简化版

	MYSQL:
	IFNULL(expression,value)
	MSSQLServer:
	ISNULL(expression,value)
	Oracle:
	NVL(expression,value)
	这几个函数的功能和COALESCE(expression,value)是等价的

MYSQL:

	SELECT FBirthDay,FRegDay,
	IFNULL(FBirthDay,FRegDay) AS ImportDayFROM T_Person

Oracle:

	SELECT FBirthDay,FRegDay,
	NVL(FBirthDay,FRegDay) AS ImportDay
	FROM T_Person


2.NULLIF()函数

主流数据库都支持NULLIF()函数，这个函数的参数格式如下：
NULLIF ( expression1 , expression2 )
如果两个表达式不等价，则 NULLIF 返回第一个 expression1的值。如果两个表达式等
价，则 NULLIF 返回第一个 expression1类型的空值。也就是返回类型与第一个 expression
相同

总是返回第一个表达式

	SELECT FBirthDay,FRegDay,
	NULLIF(FBirthDay,FRegDay)
	FROM T_Person


3.CASE函数

>用法一

	CASE expression
	WHEN value1 THEN returnvalue1
	WHEN value2 THEN returnvalue2
	WHEN value3 THEN returnvalue3
	……
	ELSE defaultreturnvalue
	END

CASE函数对表达式expression进行测试，如果expression等于value1则返回returnvalue1，
如果expression等于value2则返回returnvalue2，expression等于value3则返回returnvalue3，……
以此类推，如果不符合所有的WHEN条件，则返回默认值defaultreturnvalue

可见CASE函数和普通编程语言中的SWITCH……CASE语句非常类似；CASE函数在制作报表的时候非常有用

	SELECT
	FName,
	(CASE FLevel WHEN 1 THEN 'VIP客户'
	WHEN 2 THEN '高级客户'
	WHEN 3 THEN '普通客户'
	ELSE '客户类型错误'
	END) as FLevelName
	FROM T_Customer

>用法二

	CASE
	WHEN condition1 THEN returnvalue1
	WHEN condition 2 THEN returnvalue2
	WHEN condition 3 THEN returnvalue3
	……
	ELSE defaultreturnvalue
	END

其中的condition1 、condition 2、condition 3……为条件表达式，CASE函数对各个表达
式从前向后进行测试，如果条件condition1为真则返回returnvalue1，否则如果条件condition2
为真则返回returnvalue2，否则如果条件condition3为真则返回returnvalue3，……以此类推

	SELECT
	FName,
	FWeight,
	(CASE
	WHEN FWeight<40 THEN 'thin'
	WHEN FWeight>50 THEN 'fat'
	ELSE 'ok'
	END) as isnormal
	FROM T_Person


4.MYSQL中的独有函数

>IF()函数   相当于 三元表达式

	IF(expr1,expr2,expr3)

如果 expr1 为真(expr1 <> 0 以及 expr1 <> NULL)，那么 IF() 返回 expr2，否则返回
expr3。IF()返回一个数字或字符串，这取决于它被使用的语境

	SELECT
	FName,
	FWeight,
	IF(FWeight>50,'太胖','正常') AS ISTooFat
	FROM T_Person

>CONV()函数

CONV()函数用于对数字进行进制转换，比如将十进制的26转换为2进制显示，其参数格
式如下：

	CONV(N,from_base,to_base)

将数字 N 从 from_base进制转换到 to_base进制，并以字符串表示形式返回。from_base
和to_base的最小值为 2，最大值为 36。如果 to_base 是一个负值，N 将被看作为是一个有
符号数字。否则，N 被视为是无符号的

	SELECT CONV('26',10,2), CONV(26,10,2),CONV('7D',16,8)

MYSQL提供了简化调用的函数
BIN(N)、OCT(N)、HEX(N)它们分别用于返回 N的字符串表示的二进制、八进制值和十六
进制形式

	SELECT FWeight,Round(FWeight),
	BIN(Round(FWeight)) as b,
	OCT(Round(FWeight)) as o,
	HEX(Round(FWeight)) as h
	FROM T_Person

>填充函数

在MYSQL中提供了LPAD()、RPAD()函数用于对字符串进行左填充和右填充，其参数格式如下：

	LPAD(str,len,padstr)
	RPAD(str,len,padstr)

用字符串 padstr 对 str 进行左（右）边填补直至它的长度达到 len 个字符长度，然后
返回 str。如果 str 的长度长于 len'，那么它将被截除到 len 个字符

SELECT FName,LPAD(FName,5,'*'),RPAD(FName,5,'*') FROM T_Person

>REPEAT()函数

REPEAT()函数用来得到一个子字符串重复了若干次所组成的字符串，其参数格式如下：
	REPEAT(str,count)
参数str为子字符串，而count为重复次数

	SELECT REPEAT('*',5), REPEAT('OK',3)

MYSQL中提供了一个简化REPEAT()的函数SPACE(N)，它用来得到一个有 N 空格字符
组成的字符串，可以看做是REPEAT(' ',N)的等价形式

>字符串颠倒

REVERSE()函数用来将一个字符串的顺序颠倒，下面的SQL语句将所有人员的姓名进行了颠倒：

	SELECT FName, REVERSE(FName) FROM T_Person

>字符串的集合操作

MYSQL中提供了几个字符串集合操作函数，分别是ELT()、FIELD()和FIND_IN_SET()

	ELT(N,str1,str2,str3,...)

如果 N = 1，返回 str1，如果N = 2，返回 str2，等等。如果 N 小于 1 或大于参数的
数量，返回 NULL

	SELECT
	FName,
	ELT(FLevel, 'VIP客户', '高级客户', '普通客户')
	FROM T_Customer

**简记： 1234... 对应  文本的表示**

FIELD()函数用于计算字符串在一个字符串集合中的位置，它可
以看做是ELT()的反函数。FIELD()函数的参数格式如下：

	FIELD(str,str1,str2,str3,...)

返回 str 在列表 str1, str2, str3, ... 中的索引。如果没有发现匹配项，则返回 0

	SELECT
	FName,
	FIELD(FCustomerTypeName, 'VIP', '会员', '普通客户')
	FROM T_Customer

**简记： [str1,str2,str3...] 对应  键值**

MYSQL中提供了FIND_IN_SET()函数，它用一个分隔符分割的字符串做为待匹配字符串集合，它的参数格式如下：

	FIND_IN_SET(str,strlist)

如果字符串 str 在由 N 个子串组成的列表 strlist 中，返回它在strlist中的索引次
序（从1开始计数）。一个字符串列表是由通过字符 “,” 分隔的多个子串组成。如果 str 在
不 strlist 中或者如果 strlist 是一个空串，返回值为 0。如果任何一个参数为 NULL，
返回值也是 NULL。如果第一个参数包含一个 “,”，这个函数将抛出错误信息

	SELECT FIND_IN_SET('b','a,b,c,d') as f1,
	FIND_IN_SET('d','a,b,c,d') as f2,
	FIND_IN_SET('w','a,b,c,d') as f3

**简记： str 在 strlist 第几个**

>计算集合中的最大最小值

MYSQL中的GREATEST()函数和LEAST()函数用于计算一个集合中的最大和最小值，它
们的参数个数都是不定的，也就是它们可以对多个值进行比较

	SELECT 
	GREATEST(2,7,1,8,30,4,3,99,2,222,12),
	LEAST(2,7,1,8,30,4,3,99,2,222,12)

>辅助功能函数

DATABASE()函数返回当前数据库名；
VERSION()函数以一个字符串形式返回MySQL 服务器的版本；
USER()函数（这个函数还有SYSTEM_USER、SESSION_USER两个别名）返回当前 MySQL 用户名

	SELECT DATABASE(),VERSION(),USER()


ENCODE(str,pass_str)函数使用 pass_str 做为密钥加密 str，函数的返回结果
是一个与 string 一样长的二进制字符。如果希望将它保存到一个列中，需要使用 BLOB
列类型。

与ENCODE()函数相反，DECODE()函数使用 pass_str 作为密钥解密经ENCODE加密
后的字符串 crypt_str

	SELECT FName,
	Length(ENCODE(FName,'aha')) as length,
	ENCODE(FName,'aha')  encode,
	DECODE(ENCODE(FName,'aha'),'aha') as decode
	FROM T_Person

除了加解密函数，MYSQL中还提供了对摘要算法的支持，MD5(string)、
SHA1(string)两个函数就是分别用来使用MD5算法和SHA1算法来进行字符串的摘
要计算的函数

	SELECT FName,
	MD5(FName),
	SHA1(FName)
	FROM T_Person

UUID算法来生成一个唯一的字符串序列被越来越多的开发者所使用，MYSQL中也
提供了对UUID算法的支持，UUID()函数就是用来生成一个UUID字符串的，使用方法如下：

	SELECT UUID(),UUID()


5.Oracle 中的独有函数

>填充函数

与MYSQL类似，Oracle中也提供了用于进行字符串填充的函数LPAD()、RPAD()，其参
数格式如下：

	LPAD(char1,n [,char2])
	RPAD(char1, n [,char2])

与MYSQL中不同的是，Oracle中LPAD()和RPAD()函数的第三个参数是可以省略的，如
果省略第三个参数，则使用单个空格进行填充

	SELECT FName,
	LPAD(FName,5,'*'),
	RPAD(FName,5,'#')
	FROM T_Person

>返回当月最后一天

Oracle 中的 LAST_DAY()函数可以用来计算指定日期所在月份的最后一天的日期

	SELECT FName,FBirthDay,
	LAST_DAY(FBirthDay)
	FROM T_Person
	WHERE FBirthDay IS NOT NULL

>计算最大最小值

和MYSQL类似，Oracle中提供了用来计算一个集合中的最大和最小值的GREATEST()
函数和LEAST()函数。其使用方法和MYSQL一致

	SELECT GREATEST(2,7,1,8,30,4,5566,99,2,222,12),
	LEAST(2,7,1,8,30,4,3,99,-2,222,12)
	FROM DUAL

>辅助功能函数

USER 函数用来取得当前登录用户名，注意使用这个函数的时候不能使用括号形式的空
参数列表，也就是 USER()这种使用方式是不对的。正确使用方式如下：

	SELECT USER FROM DUAL

USERENV()函数用来取得当前登录用户相关的环境信息，这个函数的返回值为字符串
类型，需要根据情况将返回值转换为合适的类型。它的参数格式如下：
	USERENV(option)
option 参数为要取得的环境信息的名称

	SELECT USERENV('ISDBA') AS ISDBA,
	USERENV('LANGUAGE') AS LANGUAGE,
	USERENV('LANG') AS LANG
	FROM DUAL

## 字符串的拼接

	SELECT '12'+'33',FAGE+'1' FROM T_EMP

> MYSQL

在MYSQL中,当用加号"+"连接两个字段（或者多个字段）的时候,
MYSQL会尝试将字段值转换为数字类型（如果转换失败则认为字段值为0），然后进行字段的加法运算

1.在MYSQL中进行字符串的拼接要使用CONCAT函数,CONCAT函数支持一个或者多个参数，
参数类型可以为字符串类型也可以是非字符串类型,
CONCAT函数会将所有参数按照参数的顺序拼接成一个字符串做为返回值

	SELECT CONCAT('工号为:',FNUMBER,'的员工的幸福指数:',FSALARY/(FAGE-21)) FROM T_EMP

2.CONCAT_WS可以在待拼接的字符串之间加入指定的分隔符，它的第一个参
数值为采用的分隔符，而剩下的参数则为待拼接的字符串值

	SELECT CONCAT_WS(','FNUMBER,FAGE,FDEPARTMENT,FSALARY) FROM T_EMP


> ORACLE

Oracle中使用"||"进行字符串拼接

	SELECT '工号为'||FNUMBER||'的员工姓名为'||FNAME FROM T_EMP WHERE FNAME IS NOT NULL

Oracle还支持使用CONCAT()函数进行字符串拼接

	SELECT CONCAT('年龄',FAGE) FROM T_EMP


注意点：与MYSQL的CONCAT()函数不同，Oracle的CONCAT()函数只支持两个参数，不支持两
个以上字符串的拼接


## 联合结果集

1.UNION可以连接多个结果集，就像"+"可以连接多个数字一样简单，只要在每个结果集之间加入UNION即可

	SELECT FNUMBER,FNAME,FAGE FROM T_EMP 
	UNION 
	SLECT FIDCARDNUMBER,FNAME,FAGE FROM T_EMP2


> 原则

一是每个结果集必须有相同的列数

	SELECT FNUMBER,FNAME,FAGE,FDEPARTMENT FROM T_EMP
	UNION
	SELECT FIDCARDNUMBER,FNAME,FAGE FROM T_EMP2 //报错

//解决方案  ==> 常量补充不足列

二是每个结果集的列必须类型相容（数据类型必须相同或者能够转换为同一种数据类型）


2. 如果需要在联合结果集中返回所有的记录而不管它们是否唯一，则需要在UNION运算符后使用ALL操作符

	SELECT FNAME,FAGE FROM T_EMP
	UNION ALL
	SELECT FNAME,FAGE FROM T_EMP2

应用：报表

	SELECT FNUMBER,FASALRY FROM T_EMP
	UNION
	SELECT '工资合计',SUM(FSALARY) FROM T_EMP


>注意：
mysql

	SELECT '以下是正式员工的姓名' 

oracle

	SELECT '以下是正式员工的姓名' FROM DUAL (DUAL内置表)



## 索引与约束

#### 索引

1.创建索引   CREATE INDEX 索引名 ON 表名(字段 1, 字段 2,……字段 n)

	CREATE INDEX idx_person_name ON T_Person(FName)
	
	CREATE INDEX idx_person_nameage ON T_Person(FName,FAge)

2.删除索引

>mysql  DROP INDEX 索引名 ON 表名

	DROP INDEX idx_person_name ON T_Person
	
	DROP INDEX idx_person_nameage ON T_Person

>MSSQLServer

	DROP INDEX T_Person.idx_person_name
	
	DROP INDEX T_Person.idx_person_nameage

>Oracle 和 DB2 的 DROP INDEX 语句不要求指定表名，只要指定索引名即可

	DROP INDEX idx_person_name
	
	DROP INDEX idx_person_nameage


### 约束

数据库系统中主要提供了如下几种约束：非空约束；唯一约束； CHECK 约束；主键约束；外键约束。

 1.非空约束    NOT NULL
 
	CREATE TABLE T_Person (
		FNumber VARCHAR(20) NOT NULL ,//非空约束
		FName
		VARCHAR(20),
		FAge INT
	)
	
	CREATE TABLE T_Person (
		FNumber VARCHAR2(20) NOT NULL ,//非空约束
		FName VARCHAR2(20),
		FAge NUMBER (10)
	)

inser into, update 报错：

不能将值 NULL 插入列 'FNumber'，表 'demo.dbo.T_Person'；列不允许有空值。INSERT 失败

不能将值 NULL 插入列 'FNumber'，表 'demo.dbo.T_Person'；列不允许有空值。INSERT 失败

2.唯一约束 UNIQUE

单字段唯一约束

	CREATE TABLE T_Person (
		FNumber VARCHAR(20) UNIQUE,//唯一约束
		FName VARCHAR(20),
		FAge INT
	)

	CREATE TABLE T_Person (
		FNumber VARCHAR2(20) UNIQUE,//唯一约束
		FName VARCHAR2(20),
		FAge NUMBER (10)
	)

复合唯一约束： CONSTRAINT 约束名  UNIQUE(字段 1,字段 2……字段 n) 

	CREATE TABLE T_Person (
		FNumber VARCHAR(20),
		FDepartmentNumber VARCHAR(20),
		FName VARCHAR(20),
		FAge INT,
		CONSTRAINT unic_dep_num UNIQUE(FNumber,FDepartmentNumber)
	)

	CREATE TABLE T_Person (
		FNumber VARCHAR2(20),
		FDepartmentNumber VARCHAR(20),
		FName VARCHAR2(20),
		FAge NUMBER (10),
		CONSTRAINT unic_dep_num UNIQUE(FNumber,FDepartmentNumber)
	)

添加约束：ALTER TABLE 表名  ADD CONSTRAINT 唯一约束名 UNIQUE(字段 1,字段 2……字段 n)

	mysql,oracle
	ALTER TABLE T_Person ADD CONSTRAINT unic_3 UNIQUE(FName, FAge)

	mysql
	ALTER TABLE T_Person DROP INDEX unic_1;

inser into, update 报错：

违反了 UNIQUE KEY 约束 'UQ__T_Person__1A14E395'。不能在对象 'dbo.T_Person' 中插入重复键

违反了 UNIQUE KEY 约束 'unic_dep_num'。不能在对象 'dbo.T_Person' 中插入重复键


3.CHECK 约束


	CREATE TABLE T_Person (
		FNumber VARCHAR(20) CHECK (LENGTH(FNumber)>12),//CHECK 约束
		FName VARCHAR(20),
		FAge INT CHECK(FAge >0),//CHECK 约束
		FWorkYear INT CHECK(FWorkYear>0)//CHECK 约束
	)


	CREATE TABLE T_Person (
		FNumber VARCHAR2(20) CHECK (LENGTH(FNumber)>12),//CHECK 约束
		FName VARCHAR2(20),
		FAge NUMBER (10) CHECK(FAge >0),//CHECK 约束
		FWorkYear NUMBER (10) CHECK(FWorkYear>12)//CHECK 约束
	)

注意点：
CHECK 子句添加 CHECK 约束的方式的缺点是约束条件不能引用其他列

	... FWorkYear INT CHECK(FWorkYear< FAge)) ...

解决：
如果希望 CHECK子句中的条件语句中使用其他列，则必须在 CREATE TABLe 语句的末尾使用 CONSTRAINT 关键字定义它
CONSTRAINT 约束名 CHECK(约束条件)

	CREATE TABLE T_Person (
		FNumber VARCHAR(20),
		FName VARCHAR(20),
		FAge INT,
		FWorkYear INT,
		CONSTRAINT ck_1 CHECK(FWorkYear< FAge)
	)

inser into, update 报错：

INSERT 语句与 CHECK 约束"CK__T_Person__FWorkY__24927208"冲突。该冲突发生于数据库"demo"，表
"dbo.T_Person", column 'FWorkYear'

INSERT 语句与 CHECK 约束"ck_1"冲突。该冲突发生于数据库"demo"，表"dbo.T_Person"


4.主键约束  主键必须能够唯一标识一条记录，也就是主键字段中的值必须是唯一的，而且不能包含NULL 值

单一字段组成的主键

	CREATE TABLE T_Person (
		FNumber VARCHAR(20) PRIMARY KEY,
		FName VARCHAR(20),
		FAge INT
	)

	CREATE TABLE T_Person (
		FNumber VARCHAR2(20) PRIMARY KEY,
		FName VARCHAR2(20),
		FAge NUMBER (10)
	)

复合主键或者联合主键

	CREATE TABLE T_Person (
		FNumber VARCHAR(20),
		FName VARCHAR(20),
		FAge INT,
		CONSTRAINT pk_1 PRIMARY KEY(FNumber,FName)
	)

	CREATE TABLE T_Person (
		FNumber VARCHAR2(20)
		FName VARCHAR2(20),
		FAge NUMBER (10) ,
		CONSTRAINT pk_1 PRIMARY KEY(FNumber,FName)
	)

添加主键

	ALTER TABLE T_Person
	ADD CONSTRAINT pk_1 PRIMARY KEY(FNumber,FName)

删除主键

	ALTER TABLE T_Person DROP PRIMARY KEY


5.外键约束

添加外键

	ALTER TABLE T_Book
	ADD CONSTRAINT fk_book_author
	FOREIGN KEY (FAuthorId) REFERENCES T_AUTHOR(FId)

删除外键

	DROP TABLE T_Book;
	DROP TABLE T_AUTHOR;

先删除T_Book



## 表连接

#### 内连接（INNER JOIN）

>INNER JOIN table_name ON condition

在 INNER JOIN 关键字后指明要被连接的表，而在 ON 关键字后则指定了进行连接时所使用的条件

*内部连接要求组成连接的两个表必须具有匹配的记录*

	SELECT T_Order.FId,FNumber,FPrice FROM T_Order 
	INNER JOIN T_Customer
	ON FCustomerId = T_Customer.FId  // T_ORDER FCustomerId
	WHERE T_Customer.FName ='TOM'

注意： 

为了避免列名歧义并且提高可读性，这里建议使用表连接的时候要显式列所属的表

INNER JOIN 中的 INNER 是可选的，INNER JOIN 是默认的连接方式

表连接的时候可以不局限于只连接两张表，因为有很多情况下需要联系许多表

简化

	SELECT o.FId,o.FNumber,o.FPrice,c.FName,c .FAge 
	FROM T_Order o
	JOIN T_Customer c
	ON o.FCustomerId= c.FId
	WHERE c.FName='TOM'


	SELECT T_Order.FNumber,T_Order.FPrice,T_Customer.FName,T_Customer.FAge
	FROM T_Order
	JOIN T_Customer
	ON T_Order.FPrice< T_Customer.FAge*5
	and T_Order.FCustomerId=T_Customer.FId //and


#### 交叉连接

隐式

	SELECT c.FId, c.FName, c.FAge,o.FId, o.FNumber, o.FPrice
	FROM T_Customer c, T_Order o

显式 CROSS JOIN 

	SELECT c.FId, c.FName, c.FAge,
	o.FId, o.FNumber, o.FPrice
	FROM T_Customer c
	CROSS JOIN T_Order o

>注意：

交叉连接的显式定义方式为使用CROSS JOIN关键字，其语法与INNER JOIN类似

使用CROSS JOIN的方式声明的交叉连接只能被MYSQL、MSSQLServer和Oracle所支
持，在DB2中是不被支持的

因为所有的数据库系统都支持隐式的交叉连接，所以它是执行
交叉连接的最好方法


#### 自连接

>交叉连接、内连接、外连接等连接方式中只要参与连接的表是同一张表，那么它们就可以被称为自连接

	SELECT o1.FNumber,o1.FPrice,o1.FTypeId,o2.FNumber,o2.FPrice,o2.FTypeId
	FROM T_Order o1
	INNER JOIN T_Order o2
	ON o1.FTypeId=o2.FTypeId and o1.FId<o2.FId

>注意

	SELECT FNumber,FPrice,FTypeId
	FROM T_Order
	WHERE FTypeId= FTypeId//无意义

	SELECT FNumber,FPrice,FTypeId
	FROM T_Order o
	INNER JOIN T_Order
	ON T_Order.FTypeId=T_Order.FTypeId//T_Order报错

	SELECT o1.FNumber,o1.FPrice,o1.FTypeId,
	o2.FNumber,o2.FPrice,o2.FTypeId
	FROM T_Order o1
	INNER JOIN T_Order o2
	ON o1.FTypeId=o2.FTypeId and o1.FId<>o2.FId //<>

1.注意sql语句的写法，否组无意义

2.同一张表需要指定不同的别名   (T_Order => o1,o2)

3.数据库系统把“A匹配B”与“B匹配A”看成了两个不同的匹配，导致数据重复,所以and后面的条件非常重要


#### 外部连接

外部连接的语法与内部连接几乎是一样的，主要区别就是对于空值的处理

外部连接不需要两个表具有匹配记录

外部连接分为三种类型：右外部连接（RIGHT OUTER JOIN）、左外部连接（LEFT OUTER JOIN）和全外部连接（FULL OUTER JOIN

三者不同点说明如下：

左外部连接还返回左表中不符合连接条件的数据；

右外部连接还返回右表中不符合连接条件的数据；

全外部连接还返回左表中不符合连接条件的数据以及右表中不符合连接条件的数据，它其实是左外部连接和左外部连接的合集

（这里的左表和右表是相对于JOIN关键字来说的，位于JOIN关键字左侧的表即被称为左表，而位于JOIN关键字右侧的表即被称为右表）

1.左外部连接

	SELECT o.FNumber,o.FPrice,o.FCustomerId,
	c.FName,c.FAge
	FROM T_Order o
	LEFT OUTER JOIN T_Customer c
	ON o.FCustomerId=c.FId

2.右外部连接

	SELECT o.FNumber,o.FPrice,o.FCustomerId,
	c.FName,c.FAge
	FROM T_Order o
	RIGHT OUTER JOIN T_Customer c
	ON o.FCustomerId=c.FId

3.全外部连接

几乎所有的数据库系统都支持左外部连接和右外部连接，但是全外部连接则不是所有数据库系统都支持，特别是最常使用的MYSQL就不支持全外部连接

	SELECT o.FNumber,o.FPrice,o.FCustomerId,
	c.FName,c.FAge
	FROM T_Order o
	FULL OUTER JOIN T_Customer c
	ON o.FCustomerId=c.FId

mysql中使用左外部连接 、右外部连接 和UNION

	SELECT o.FNumber,o.FPrice,o.FCustomerId,c.FName,c.FAge
	FROM T_Order o
	LEFT OUTER JOIN T_Customer c
	ON o.FCustomerId=c.FId
	
	UNION
	
	SELECT o.FNumber,o.FPrice,o.FCustomerId,c.FName,c.FAge
	FROM T_Order o
	RIGHT OUTER JOIN T_Customer c
	ON o.FCustomerId=c.FId

>注意： 外连接返回的不符合条件的语句  通过where条件来进行过滤掉的

**表连接总计**

1.当只要取出数据，不带任何条件可以使用   union 、  交叉连接隐式  来实现
	
	SELECT T1.* FROM TABLE1 T1  UNION  SELECT T2.* FROM TABLE2 T2
	
	SELECT T1.* ,T2.* FROM TABLE1 TA,TABLE2 T2

2.多表具有匹配记录使用内连接  inner  join ... on ...

3.空值处理，不具有匹配记录使用外连接  left/right join ... on ...

4.外连接可以实现内连接的功能

## 子查询

查询的语法与普通的 SELECT 语句语法相同，所有可以在普通 SELECT 语句中使用
的特性都可以在子查询中使用，比如 WHERE 子句过滤、UNION 运算符、HAVING 子句、
GROUPBY 子句、ORDER BY 子句，甚至在子查询中还可以包含子查询。
同时，不仅可以在 SELECT 语句中使用子查询，还可以在 UPDATE、DELETE 等语句中使用子查询

SELECT 语句可以嵌套在其他语句中,比如 SELECT,INSERT,UPDATE 以及 DELETE 等，这些被嵌套的 SELECT 语句就称为子查询

1.单值子查询 （标量子查询）

注意：子查询的返回值必须只有一行记录，而且只能有一个列

标量子查询可以用在 SELECT 语句的列表中、表达式中、WHERE 语句中等很多场合


2.列值的子查询 （表子查询）

列值子查询可以返回一个多行多列的结果集

表子查询可以用在 SELECT 语句的 FROM子句中、INSERT 语句、连接、IN 子句等很多场合

注意：表子查询可以看作一张临时的表，所以引用子查询中列的时候必须使用子查询中定义的列名，也就是如果子查询中为列定义了别名，那么在引用的时候也要使用别名

	SELECT T_Reader.FName,t2.FYear,t2.FName ,t2.F3
	FROM T_Reader,
	(SELECT FYearPublished AS FYear,FName,1+2 as F3 FROM T_Book WHERE FYearPublished < 1800) t2

这里的表子查询为 FYearPublished 列取了一个别名 FYear，这样在引用它的时候就必须使用 FYear 而不能继续使用 FYearPublished 


## 知识拓展

#### SQL 注入漏洞攻防

对付 SQL 注入漏洞有两种方式：过滤敏感字符和使用参数化 SQL、

1.过滤敏感字符

过滤or、and、select、delete 之类的字符串片段

缺点：

给正常用户的正常操作造成了麻烦

逻辑难以严谨

2.使用参数化 SQL

 Java、C#会直接将参数化 SQL 以及对应的参数值传递给 DBMS，
在 DBMS 中会将参数值当成一个普通的值来处理而不是将它们拼接到参数化 SQL 中，因此
从根本上避免了 SQL 注入漏洞攻击

ASP、PHP 等语言没有提供参数化 SQL 机制，因此只能采用其它方式来避免了 SQL 注入漏洞攻击


#### SQL 调优

“二八原理”

借助工具发现占用系统资源排在前面的 SQL 语句，然后尝试对它们进行优化，优化后再次执行分析，迭代这一过程，直到系统中没有明显的系统资源消耗异常的SQL 语句为止

索引是数据库调优的最根本的优化方法
