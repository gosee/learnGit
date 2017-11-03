echo "删除多余包"
/bin/rm hiips-common_1.0.jar
/bin/rm hiips-common.jar
/bin/rm himonitor.jar
/bin/rm hiparam.jar
/bin/rm hiprt.jar
/bin/rm hipubatc.jar
/bin/rm securityAPI.jar
echo "软连接jar"
ln -s ../app/applib/hiips-common_1.0.jar
ln -s  ../app/applib/hiips-common.jar
ln -s ../applib/himonitor.jar
ln -s ../applib/hiparam.jar
ln -s ../applib/hiprt.jar
ln -s ../applib/hipubatc.jar
ln -s ../app/applib/securityAPI.jar
