# 正则表达式引擎实现

## 实现原理

**正则表达式**的本质是**正则文法**，只是为了方便实际使用而增加了许多语法糖。
因此实现一个正则表达式引擎的根本是实现一个正则文法的引擎。

实现正则文法的引擎的步骤如下：
1. 从正则文法生成NFA
1. 将NFA转化为DFA
1. DFA最小化
1. 通过DFA生成转移表进行字符的匹配

项目目前只实现了正规文法的引擎，各个步骤的具体实现代码中均有描述，在此不赘述。

## TODO

- [ ] 直接解析正则表达式而不是正规文法 
