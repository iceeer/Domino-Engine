固定域：
静态标识	$ID 编辑文本域 "S" + @Text(@DocumentUniqueID)
创建日期	$Created 编辑日期域 @Created
更行日期 	$Updated 编辑日期多值域
创建人		$CreatedBy 编辑姓名域 @UserName
作者		$AuthorList 编辑作者域 @UserName:"LocalDomainAdmins":"[manager]":"[supervisor]"
读者		$ReaderList 编辑读者域
删除标记 	$Deleted 是否删除(yes:删除)
File 文件（富文本域）
$RID 关联标识

$Subject
$Tags
$

视图：
$$All 所有文档（不包括设计）
$$Conflicts 所有冲突文档
$$Design 所有设计文档
$$Locked 所有锁定的文档
$$SoftRemoved 所有软删除的文档

参数
menu.login.type=sso
sso.enable=true
sso.josso.url=http://10.136.238.173:8888/josso/


empp.host=211.136.163.68
empp.port=9981
empp.account=10657001021049
empp.user=admin
empp.password=abcd1234


基础数据库
基础模板 base\base.ntf 模板名:BASE
引擎库 base\engine.nsf 模板名:BENG
应用库 base\application.nsf 模板名:BAPP
配置库 base\setting.nsf 模板名:BSET
日记库 base\log.nsf 模板名:BLOG

数据库：
引擎库 engine.nsf 代码：显示、控制
应用库 application.nsf 数据（变化大、量大）
配置库 setting.nsf
日记库 log.nsf

XPage
index.xsp?do=XX 调用参数do.XX配置的类
index.xsp?do=login 登陆链接
index.xsp?do=logout 登出链接
index.xsp?do=message 提示信息链接
index.xsp?do=doc&id=<$ID>
index.xsp?do=doc&db=<setting|application|log>&id= setting.db.file.name配置的数据库（数据库路径为相对路径）



extmgr_addins=trigger.dll
TriggerHappyConfigDb=base\auditmanager.nsf