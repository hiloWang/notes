//========================================================================
// 面向对象技术：类的层次结构和抽象类
//========================================================================


//这个函数可以做任何抽象方法
function abstractmethod() {
    throw new Error("abstract method");
}

/*
 *  AbstractSet 定义了一个抽象方法：contains().
 */
function AbstractSet() {
    throw new Error("Can't instantiate abstract classes");
}

AbstractSet.prototype.contains = abstractmethod;

/*
 * NotSet是AbstractSet的非抽象子类，所有不在其他集合中的成员都在这个集合中，
 * 因为它是在其他集合不可写的条件下定义的，同时由于它的成员是无数个，因此它是被可枚举的，
 * 我们只能用它来及检测元素成员的归属情况。
 */
var NotSet = AbstractSet.extend(
    //子类构造函数
    function NotSet(set) {
        this.set = set;
    },
    //实例方法
    {
        contains: function (x) {
            return !this.set.contains(x);
        },
        toString: function (x) {
            return "~" + this.set.toString();
        },
        equals: function (that) {
            return that instanceof NotSet && this.set.equals(that.set);
        }
    }
);


/*
 * AbstractEnumerableSet是AbstractSet的抽象子类， 它定义了抽象方法 size() 和 foreach()，
 * 然后实现了非抽象方法：isEmpty(), toArray(), toString(), toLocaleString(), 和 equals()方法，
 * 子类实现了contains(), size(), and foreach()，这三个方法可以很轻易的调用这5个抽象方法
 */
var AbstractEnumerableSet = AbstractSet.extend(
    //子类构造函数
    function () {
        throw new Error("Can't instantiate abstract classes");
    },
    //实例方法
    {
        //抽象方法
        size: abstractmethod,
        foreach: abstractmethod,
        //非抽象方法
        isEmpty: function () {
            return this.size() == 0;
        },
        toString: function () {
            var s = "{", i = 0;
            this.foreach(function (v) {
                if (i++ > 0) s += ", ";
                s += v;
            });
            return s + "}";
        },
        toLocaleString: function () {
            var s = "{", i = 0;
            this.foreach(function (v) {
                if (i++ > 0) s += ", ";
                if (v == null) s += v; // null & undefined
                else s += v.toLocaleString(); // all others
            });
            return s + "}";
        },
        toArray: function () {
            var a = [];
            this.foreach(function (v) {
                a.push(v);
            });
            return a;
        },
        equals: function (that) {
            if (!(that instanceof AbstractEnumerableSet)) return false;
            // If they don't have the same size, they're not equal
            if (this.size() != that.size()) return false;
            // Now check whether every element in this is also in that.
            try {
                this.foreach(function (v) {
                    if (!that.contains(v)) throw false;
                });
                return true;  // All elements matched: sets are equal.
            } catch (x) {
                if (x === false) return false; // Sets are not equal
                throw x; // Some other exception occurred: rethrow it.
            }
        }
    });

/*
 * SingletonSet 是 AbstractEnumerableSet的子类，
 * SingletonSet只有一个元素且它是只读的。
 */
var SingletonSet = AbstractEnumerableSet.extend(
    //子类构造函数
    function SingletonSet(member) {
        this.member = member;
    },
    //实例方法
    {
        contains: function (x) {
            return x === this.member;
        },
        size: function () {
            return 1;
        },
        foreach: function (f, ctx) {
            f.call(ctx, this.member);
        }
    }
);


/*
 * AbstractWritableSet 是 AbstractEnumerableSet.
 * 它定义了抽象方法： add() and remove(),
 * 它实现了 union(), intersection(), and difference() 方法
 */
var AbstractWritableSet = AbstractEnumerableSet.extend(
    //子类构造函数
    function () {
        throw new Error("Can't instantiate abstract classes");
    },
    //对象方法
    {
        add: abstractmethod,
        remove: abstractmethod,
        union: function (that) {
            var self = this;
            that.foreach(function (v) {
                self.add(v);
            });
            return this;
        },
        intersection: function (that) {
            var self = this;
            this.foreach(function (v) {
                if (!that.contains(v)) self.remove(v);
            });
            return this;
        },
        difference: function (that) {
            var self = this;
            that.foreach(function (v) {
                self.remove(v);
            });
            return this;
        }
    });

/*
 *ArraySet是AbstractWritableSet的子类
 * 它数字的形式定义集合中的元素,
 * 对于contains方法它使用了数组的线性搜索，
 * 因为 contains()方法的复杂度是 O(n) 而不是 O(1),
 * 它非常适用于相对小型的集合，注意，这里的实现用到了ES5的数组方法indexOf和forEach()
 */
var ArraySet = AbstractWritableSet.extend(
    //子类构造函数
    function ArraySet() {
        this.values = [];
        this.add.apply(this, arguments);
    },
    //对象方法
    {
        contains: function (v) {
            return this.values.indexOf(v) != -1;
        },
        size: function () {
            return this.values.length;
        },
        foreach: function (f, c) {
            this.values.forEach(f, c);
        },
        add: function () {
            for (var i = 0; i < arguments.length; i++) {
                var arg = arguments[i];
                if (!this.contains(arg)) this.values.push(arg);
            }
            return this;
        },
        remove: function () {
            for (var i = 0; i < arguments.length; i++) {
                var p = this.values.indexOf(arguments[i]);
                if (p == -1) continue;
                this.values.splice(p, 1);
            }
            return this;
        }
    }
);
