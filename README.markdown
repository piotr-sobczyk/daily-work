DialyWork
==============

DialyWork is a Tomighty fork. It helps you perform tasks that you want to accomplish for fixed amount of time every day.

This approach is orthogonal to Pomodoro Technique (you can adapt Pomodoro if you wish but don't have to).

Unlike in Pomodoro, you can pause and resume at will and take breaks of arbitrary length.

Building
--------

Regardless of your operating system, you must have the following things installed on it and included in your path:

  * JRE 1.7 or greater
  * Maven 2.x or greater
  * Git

Under Windows platform you also must have:

  * NSIS 2.x or greater

Open a system shell and check out the sources into some directory. Then `cd` into that directory and type:

  `mvn clean package`

The resulting built artifacts will be located under the `target` directory. That's it.

