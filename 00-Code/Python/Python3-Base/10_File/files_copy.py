oldFileName = input("请输入要拷贝的文件名字:")

oldFile = open(oldFileName, 'r', encoding='utf8')

# 如果打开文件
if oldFile:

    # 提取文件的后缀
    fileFlagNum = oldFileName.rfind('.')
    fileFlag = ''
    if fileFlagNum > 0:
        fileFlag = oldFileName[fileFlagNum:]

    # 组织新的文件名字
    newFileName = oldFileName[:fileFlagNum] + '_copy_' + fileFlag

    # 创建新文件
    newFile = open(newFileName, 'w', encoding='utf8')

    # 把旧文件中的数据，一行一行的进行复制到新文件中
    contents = oldFile.readlines()
    for lineContent in contents:
        newFile.write(lineContent)

    # 关闭文件
    oldFile.close()
    newFile.close()
