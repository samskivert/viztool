VIZTOOL: An extensible tool for visualizing Java classes
--------------------------------------------------------

As the catchy subtitle claims, viztool is used to generate visualizations of
collections of Java classes. These visualizations are intended to be printed
out and taped to the wall or set on the desk beside you or folded into paper
airplanes and sailed around the room. Thus viztool does not include a
sophisticated user interface for viewing these presentations onscreen (use
ghostscript for that), although you can actually display them on the screen
because the rendering is done via the Java 2D rendering engine.

viztool was born from my repeated desire to be able to glance over all of the
myriad classes that come to be involved in any large project. I knew I could go
out and pay thousands of dollars for a single user license for some object
oriented design tool that would diagram my classes three ways to Sunday,
generate code, count my chickens and make toast on the side, but I couldn't
find a free, simple tool for generating basic class diagrams.

Building viztool
----------------

Building viztool is very simple. You can use either Ant or Maven. Build with
Ant like so:

% ant dist

Or build with Maven (and install into your local repository) like so:

% mvn install

Using viztool
-------------

viztool is designed to be easily invoked from Ant. Add a task like the
following to your build.xml file:

    <target name="hierviz">
      <taskdef name="viztool" classname="com.samskivert.viztool.DriverTask">
        <classpath>
          <!-- include viztool.jar and samskivert.jar; or use the Maven Ant task
               to obtain viztool and its dependencies automatically -->
        </classpath>
      </taskdef>
      <viztool pkgroot="com.samskivert.viztool"
        classes="com.samskivert.viztool.*"
        visualizer="com.samskivert.viztool.hierarchy.HierarchyVisualizer">
        <classpath refid="your_classpath"/>
      </viztool>
    </target>

the parameters passed to the task are:

* pkgroot: the base package name which will be used to strip common text from
  the front of fully qualified class names
* classes: a regular expression matching the classes to be visualized
* visualizer: the classname of the visualizer to use

the <viztool> element should contain a <classpath> element which defines the
classpath over which viztool will iterate, searching for classes that match the
specified pattern.

There is also an included shell script (bin/viztool). Add the classes that you
wish to visualize to your CLASSPATH environment variable and then invoke the
viztool script with the package prefix you wish to visualize.

For example:

% export CLASSPATH=<here>/foo.jar:<there>/bar.jar:<everywhere>/baz.jar
% ./bin/viztool --print com.whoever.mygreatpackage

Because the classes are actually resolved by the JVM when visualizing, all
classes that the visualized classes depend upon must also be loadable (meaning
included in the class path).

If you want to write your own script, take a look at the viztool script to see
what arguments to pass to the visualization driver class.

Distribution
------------

viztool is released under the GPL. The most recent version of the code is
available here:

  http://github.com/samskivert/viztool/
