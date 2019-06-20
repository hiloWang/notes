package ztiany.chapter17

//1 首先继承FactoryBuilderSupport
class RobotBuilder extends FactoryBuilderSupport {
    //2 注册个节点的工厂方法
    {
        registerFactory('robot', new RobotFactory())
        registerFactory('forward', new ForwardMoveFactory())
        registerFactory('left', new LeftTurnFactory())
    }
}

/*
1 ， 工厂扩展自AbstractFactory
2，newInstance方法用于创建对应的节点
3，isLeaf方法默认返回false，表示该节点可以有一个处理子节点的闭包，如果返回了true，还进入比表则会报错
4，onHandleNodeAttributes方法适合对属性执行特殊的处理操作，比如在ForwardMoveFactory把使用了的speed和duration去除掉，如果onHandleNodeAttributes
    返回true，会把留下来的属性填给节点
5，setChild会在副系欸但的工厂上调用，比如ForwardMove的父节点Robot的RobotFactory种，其setChild方法的child就是ForwardMove的实例
6，setParent会在子节点处理完成时调用
7，onNodeCompleted方法会在节点处理完成是调用

 */

class RobotFactory extends AbstractFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        return new Robot(name: value)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        super.setParent(builder, parent, child)
    }

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.movements << child
    }
}


class ForwardMoveFactory extends AbstractFactory {

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        new ForwardMove()//会自动将rotation: 20填充过ForwardMove
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (attributes.speed && attributes.duration) {
            node.dist = attributes.speed * attributes.duration
            //使用调用了就去掉，不然会填充给实例
            attributes.remove('speed')
            attributes.remove('duration')
        }
        true
    }

    @Override
    boolean isLeaf() {
        true
    }
}

class LeftTurnFactory extends AbstractFactory {
    @Override
    boolean isLeaf() {
        true
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        new LeftTurn()
    }
}

class Robot {
    String name
    def movements = []

    void go() {
        println "Robot $name operation..."
        movements.each {
            println it
        }
    }
}

class ForwardMove {
    def dist

    @Override
    String toString() {
        "move distance .... $dist"
    }
}

class LeftTurn {
    def rotation

    String toString() {
        "turn left ...  $rotation degress"
    }
}

//--------------------
//运行代码
//--------------------

rb = new RobotBuilder()

def robot = rb.robot("iRobot") {
    forward(dist: 20)
    left(rotation: 20)
    forward(speed: 10, duration: 2)
}
robot.go()

/*
结果：

Robot iRobot operation...
move distance .... 20
turn left ...  20 degress
move distance .... 20

 */

