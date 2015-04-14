#!/bin/sh

# This file is compatible with Pegasus 4.4.0 or later.
# this file goes to the bin directory in the directory
# pointed to by
# condor_config_val GLITE_LOCATION
#
# e.g
#  [vahi@ccg-testing2 run0007]$ condor_config_val  GLITE_LOCATION
#  /usr/libexec/condor/glite
#
#  [vahi@ccg-testing2 run0007]$ ls -lh /usr/libexec/condor/glite/
#
# [vahi@ccg-testing2 run0007]$ ls -lh /usr/libexec/condor/glite/bin/pbs_local_submit_attributes.sh
# -rwxr-xr-x 1 root root 112 Feb  7 17:24 /usr/libexec/condor/glite/bin/pbs_local_submit_attributes.sh
# [vahi@ccg-testing2 run0007]$
#
#

pbs_nodes=""

if [ ! -z $NODES ]; then
    pbs_nodes="#PBS -l nodes=${NODES}"
fi

if [ ! -z $PROCS ]; then
    pbs_nodes="${pbs_nodes}:ppn=${PROCS}"
fi

# strip any quotes . e.g 16:IB condor expects it to be quoted
# however pbs does not like it
pbs_nodes=`echo $pbs_nodes | sed 's/"//g'`
echo $pbs_nodes

if [ ! -z $WALLTIME ]; then
    # strip any quotes . e.g condor expects it to be quoted                                                                                                                                                                  
    # however pbs does not like it                                                                                                                                                                                                 
    walltime=`echo $WALLTIME | sed 's/"//g'`
    echo "#PBS -l walltime=${walltime}"
fi


if [ ! -z $PER_PROCESS_MEMORY ]; then
    # strip any quotes . e.g 1gb condor expects it to be quoted
    # however pbs does not like it
    value=`echo $PER_PROCESS_MEMORY | sed 's/"//g'`
    echo "#PBS -l pmem=${value}"
fi

if [ ! -z $TOTAL_MEMORY ]; then
    # strip any quotes . e.g 1gb condor expects it to be quoted
    # however pbs does not like it
    value=`echo $TOTAL_MEMORY | sed 's/"//g'`
    echo "#PBS -l mem=${value}"
fi

if [ ! -z $PROJECT ]; then
    echo "#PBS -A ${PROJECT}"
fi

if [ ! -z $JOBNAME ]; then
    echo "#PBS -N ${JOBNAME}"
fi

if [ ! -z $PASSENV ]  && [ $PASSENV == 1 ]; then
    echo "#PBS -V"
fi

# +remote_cerequirements = .. MYENV=="A=\"XY Z\",B=LMN"
# The temp ce-req-file generated by condor writes it as -> MYENV='"A=XY Z,B=LMN"'
# NEWENV value strips out the first and last double-quote so the value written in the generated PBS file is
if [ ! -z "$MYENV" ] ; then
    eval "NEWENV=$MYENV"
    echo "#PBS -v ${NEWENV}"
fi

# if a user passed any extra arguments set them in the end
if [ ! -z $EXTRA_ARGUMENTS ]; then
    echo "#PBS ${EXTRA_ARGUMENTS}"
fi

