package ztiany.chapter2

//--------------------
//实现接口
//--------------------

//在Groovy中，可以把一个映射或者一个代码块转换为接口，因此可以快速实现带有多个方法的接口

Button button = new Button("Button")
//java方式的注册事件
button.setOnClickListener(new Button.OnClickListener() {
    @Override
    void onClick(Button clickButton) {
        println clickButton
    }
})

button.performClick()

//groovy提供了其他方式，不需要actionPerformed方法声明，也不需要显示的new
button.setOnClickListener(
        {
            println it
        } as Button.OnClickListener //借助as操作符，相当于实现了OnClickListener接口，it表示方法的参数，当方法的参数不止一个时，可以分别定义方法参数
)

button.performClick()

//当然也可以先定义实现再作为参数传入
listener = {
    println it.name + ' listener '
}
//listener被as转化了两次
button.setOnClickListener(listener as Button.OnClickListener)
button.setOnTouchListener(listener as Button.OnTouchListener)
button.performClick()
button.performTouch()



//对于有多个方法的接口，如果打算所有的方法提供共同的实习，和下面一样不需要多做任何操作(可以把focusListener成了FocusGained和FocusLost的实现)，
focusListener = {
    println it.name + "focusListener"
}
button.seFocusListener(focusListener as Button.OnFocusListener)
button.performFocusGained()
button.performFocusLost()




//Groovy没有强制实现接口中所有的方法，可以定义自己关心的方法，而不考虑其他方法
//但是大多数情况下，大多数接口的方法都需要被实现，这时可以创建一个映射，以每个方法名作为key，以方法对一个的代码作为值传入，并且并不要实现所有的方法，
//但是如果没有提供的方法被调用了，将会抛出一个NullPointerException

handFocus = {

    focusGained:
    {
        println it.name + ' focusGained'
    }

    focusLost:
    {
        println it.name + ' focusLost'
    }
}

button.seFocusListener(handFocus as Button.OnFocusListener)
button.performFocusGained()
button.performFocusLost()







//mock Button
class Button {

      OnClickListener mClickListener
    private OnTouchListener mOnTouchListener
    private OnFocusListener mFocusListener

    final name;

    Button(name) {
        this.name = name
    }

    def setOnClickListener(OnClickListener onClickListener) {
        mClickListener = onClickListener
    }

    def setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener
    }

    def seFocusListener(OnFocusListener onFocusListener) {
        mFocusListener = onFocusListener
    }

    interface OnClickListener {
        void onClick(Button clickButton)
    }

    interface OnTouchListener {
        void onTouch(Button touchButton)
    }

    interface OnFocusListener {
        void focusGained(Button thisButton)

        void focusLost(Button thisButton)

    }


    def performClick() {
        mClickListener?.onClick(this)
    }

    def performTouch() {
        mOnTouchListener?.onTouch(this)
    }

    def performFocusGained() {
        mFocusListener?.focusGained(this)
    }

    def performFocusLost() {
        mFocusListener?.focusLost(this)
    }
}




