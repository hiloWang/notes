package ztiany.utils

file = new File(".")
file.listFiles().each {
    if (it.name.startsWith('_')) {
        name = it.name.substring(1, it.name.size())
        it.renameTo(new File(name))
    }
}