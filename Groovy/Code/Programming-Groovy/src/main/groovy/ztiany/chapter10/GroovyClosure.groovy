package ztiany.chapter10;

class GroovyClosure {

        def useClosure(closure){
            closure()
        }
        
         def passToClosure(int value , closure){
            closure(value)
        }
}