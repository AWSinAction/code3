#!/bin/bash

# ch15 -> chxx
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 15/chapter xx/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 15/Chapter xx/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter15/chapterxx/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(15\)\.[0-9]{1,2}/\1 xx/g' {} +

# ch05 -> ch15
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 5/chapter 15/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 5/Chapter 15/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter05/chapter15/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(5\)\.[0-9]{1,2}/\1 15/g' {} +

# ch06 -> ch05
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 6/chapter 5/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 6/Chapter 5/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter06/chapter05/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(6\)\.[0-9]{1,2}/\1 5/g' {} +

# ch07 -> ch06
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 7/chapter 6/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 7/Chapter 6/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter07/chapter06/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(7\)\.[0-9]{1,2}/\1 6/g' {} +

# ch08 -> ch07
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 8/chapter 7/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 8/Chapter 7/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter08/chapter07/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(8\)\.[0-9]{1,2}/\1 7/g' {} +

# ch09 -> ch08
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 9/chapter 8/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 9/Chapter 8/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter09/chapter08/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(9\)\.[0-9]{1,2}/\1 8/g' {} +

# ch10 -> ch09
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 10/chapter 9/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 10/Chapter 9/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter10/chapter09/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(10\)\.[0-9]{1,2}/\1 9/g' {} +

# ch11 -> ch10
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 11/chapter 10/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 11/Chapter 10/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter11/chapter10/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(11\)\.[0-9]{1,2}/\1 10/g' {} +

# ch12 -> ch11
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 12/chapter 11/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 12/Chapter 11/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter12/chapter11/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(12\)\.[0-9]{1,2}/\1 11/g' {} +

# ch13 -> ch12
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 13/chapter 12/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 13/Chapter 12/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter13/chapter12/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(13\)\.[0-9]{1,2}/\1 12/g' {} +

# ch14 -> ch13
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter 14/chapter 13/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter 14/Chapter 13/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter14/chapter13/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(14\)\.[0-9]{1,2}/\1 13/g' {} +

# chxx -> ch14
find . -name "*.yaml"  -exec sed -i '' -e 's/chapter xx/chapter 14/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/Chapter xx/Chapter 14/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/chapterxx/chapter14/g' {} +
find . -name "*.yaml"  -exec sed -i '' -e 's/ \(xx\)\.[0-9]{1,2}/\1 14/g' {} +

mv chapter15 chapterxx
mv chapter05 chapter15
mv chapter06 chapter05
mv chapter07 chapter06
mv chapter08 chapter07
mv chapter09 chapter08
mv chapter10 chapter09
mv chapter11 chapter10
mv chapter12 chapter11
mv chapter13 chapter12
mv chapter14 chapter13
mv chapterxx chapter14
