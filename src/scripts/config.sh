# Setup standard Republic Parameters for use in other runs.
export REPUBLIC_VERSION=0.6.2
export JAVA_HOME=/usr
export MAVEN_HOME=$HOME/.m2
export CLASSPATH=/opt/republic/target/republic-0.6.2.jar:/opt/rep_repository/castor/castor/0.9.5.3/castor-0.9.5.3.jar:/opt/rep_repository/xerces/xercesimpl/2.6.2/xercesImpl-2.6.2.jar:/opt/rep_repository/jdom/jdom/1.0/jdom-1.0.jar:/opt/rep_repository/jaxen/jaxen/1.1-beta-8/jaxen-1.1-beta-8.jar

echo $CLASSPATH
export REPUBLICSTART=org.republic.Start
#export OOCALC=oocalc2
