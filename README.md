# redis-migration
redis version of flyway

#### 缘起
在使用 flyway 管理数据库的过程中感受到实在的便利。故开发一个 redis 版本用于 redis 数据的维护。

#### 说明
启动脚本修改自 flyway 启动脚本

### 使用示例
redis-migration clean
redis-migration migrate
redis-migration info