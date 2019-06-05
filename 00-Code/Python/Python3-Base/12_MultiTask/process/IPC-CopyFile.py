from multiprocessing import Pool, Manager
import os


def copy_file_task(name, old_folder_name, new_folder_name, queue):
    """完成copy一个文件的功能"""
    fr = open(old_folder_name + "/" + name)
    fw = open(new_folder_name + "/" + name, "w")

    content = fr.read()
    fw.write(content)

    fr.close()
    fw.close()

    queue.put(name)


def main():
    # 0. 获取永远要copy的文件夹的名字
    old_folder_name = input("请输入文件夹的名字：")

    # 1. 创建一个文件夹
    new_folder_name = old_folder_name + "-复件"
    # print(newFolderName)
    os.mkdir(new_folder_name)

    # 2. 获取old文件夹中的所有的文件名字
    file_names = os.listdir(old_folder_name)
    # print(fileNames)

    # 3. 使用多进程的方式copy 原文件夹中的所有文件到新的文件夹中
    pool = Pool(5)
    queue = Manager().Queue()

    for name in file_names:
        pool.apply_async(copy_file_task, args=(name, old_folder_name, new_folder_name, queue))

    num = 0
    all_num = len(file_names)
    while num < all_num:
        queue.get()
        num += 1
        copy_rate = num / all_num
        print("copy的进度是:%.2f%%" % (copy_rate * 100), end="")

    print("\n已完成Copy")


if __name__ == "__main__":
    main()
