package ztiany.chapter19


//--------------------
//使用命令链式改进流畅性
//--------------------

class CommandChain {


    def move(dir) {
        println "moving $dir"
        this
    }
    def and(then){this}

    def turn(dir) {
        println "turning $dir"
        this
    }

    def jump(speed, dir) {
        println "jumping $speed and $dir"
        this
    }

}

def (forward, left, then, fast, right) = [
        'forward',
        'left',
        'then',
        'fast',
        'right'
]

cc = new CommandChain()
cc.with {
    move forward and then turn left
    jump fast,forward and then turn right
}

/*
moving forward
turning left
jumping fast and forward
turning right

 */