# 加密解密

## ASCII

ASCII （American Standard Code for Information Interchange，美国信息交换标准代码）是基于拉丁字母的一套计算机编码系统。它主要用于显示现代英语，而其扩展版本 EASCII 则可以部分支持其他西欧语言，并等同于国际标准ISO/IEC 646。

具体参考[wiki/ASCII](https://zh.wikipedia.org/wiki/ASCII)。

## 凯撒密码

在密码学中，凯撒密码（英语：Caesar cipher），或称凯撒加密、凯撒变换、变换加密，是一种最简单且最广为人知的加密技术。它是一种替换加密的技术，明文中的所有字母都在字母表上向后（或向前）按照一个固定数目进行偏移后被替换成密文。例如，当偏移量是3的时候，所有的字母A将被替换成D，B变成E，以此类推。这个加密方法是以罗马共和时期恺撒的名字命名的，据称当年恺撒曾用此方法与其将军们进行联系。

具体参考[wiki/凯撒密码](https://zh.wikipedia.org/wiki/%E5%87%B1%E6%92%92%E5%AF%86%E7%A2%BC)。

破解方式：

- 品读分析法，英文中出现字母最频繁的是字母 e

## Byte 和 bit

一个 Byte 有八个 bit

## 常见对称加密算法

- DES，DES 是一种将 64 比特的明文密码加密成 64 比特密文的对称密码算法，需要的密码长度为 8 位
- AES，需要的密码长度位 16 位

```
AES/CBC/NoPadding (128) 
AES/CBC/PKCS5Padding (128) 
AES/ECB/NoPadding (128) 
AES/ECB/PKCS5Padding (128) 
DES/CBC/NoPadding (56) 
DES/CBC/PKCS5Padding (56) 
DES/ECB/NoPadding (56) 
DES/ECB/PKCS5Padding (56) 
DESede/CBC/NoPadding (168) 
DESede/CBC/PKCS5Padding (168) 
DESede/ECB/NoPadding (168) 
DESede/ECB/PKCS5Padding (168) 
RSA/ECB/PKCS1Padding (1024, 2048) 
RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048) 
RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048) 
```

上面摘取自官方文档，三个单词的意思分别为`/算法/工作模式/填充模式`，最后括号里标明的是密钥长度，可以看到 DES 的密钥长度是 56 位，但是代码中我们传入的密钥长度是 8 个字符，那么 `8*8=64` 是怎么回事呢？因为DES 密钥每 8 位包含 1 位纠错码，即前 7 位参与加密计算，最后一位作为校验码，密码长度 64 位每 8 位减去一位就是 56 了。