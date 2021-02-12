common为通用服务 注册中心使用nacso

脚本下为order和account数据库sql。seata.sql为seata依赖脚本。必须导入。

linxu解压 seta-server-1.2.0包，修改 config下面 
1.file.cof中的节点 service、store。
2.registry.conf文件中 registry、config两个节点。具体配置说明看官方文档

注意事项，微服务中，对于feign调用 重试次数 重试机制 熔断都会造成数据的错乱，所以在实际项目中需要自己配置。