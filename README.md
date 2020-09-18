### 作业描述
* 将所有接口的返回值都替换成使用ResponseEntity
* 所有post请求都返回201,并且返回的头部带上index字段（值为创建的资源在列表中的位置：eg: 添加的热搜事件在列表中的index）
* 完成demo里的练习：get /rs/list和 get/rs/{index}接口返回的数据中不包含user字段
* 添加获取所有用户的接口: get /users，期望返回的数据格式例子: 
    ```
    [{
        "user_name": "xxxx",
        "user_age": 19,
        "user_gender": "female",
        "user_email": "xxx@xx",
        "user_phone": "1xxxxxxxxxx"
    }]
* 先写测试！！！
* hint: @JsonpProperty的使用

#### 实现或修改如下接口
* 修改添加热搜事件接口：参照demo，将添加RsEvent持久化到数据库中
    ```
    {
        “eventName”: “热搜事件名”,
         “keyword”: “关键字”,
         “userId”: “user_id”
    }
  ```
  其中user需要是已注册用户，否则添加失败返回400
  

* 修改删除用户接口：参照demo，删除用户时，需要同时删除该用户所创建的热搜事件(使用JPA提供的mapping注解@ManyToOne @OneToMany)
* 添加更新接口
   ```
    request: patch /rs/{rsEventId}
    requestBody: {
                    “eventName”: “新的热搜事件名”,
                     “keyword”: “新的关键字”,
                     “userId”: “user_id”
                }
   ```
  接口要求：当userId和rsEventId所关联的User匹配时，更新rsEvent信息
          当userId和rsEventId所关联的User不匹配时，返回400
          userId为必传字段
          当只传了eventName没传keyword时只更新eventName
          当只传了keyword没传eventName时只更新keyword
          
* 添加投票接口
    ```
    request: post /rs/vote/{rsEventId}
    request body: {
                    voteNum: 5,
                    userId: 1,
                    voteTime: "current time"
                  }  
    接口要求：如果用户剩的票数大于等于voteNum，则能成功给rsEventId对应的热搜事件投票
            如果用户剩的票数小于voteNum,则投票失败，返回400
            考虑到以后需要查询投票记录的需求（根据userId查询他投过票的所有热搜事件，票数和投票时间，根据rsEventId查询所有给他投过票的用户，票数和投票时间），
            创建一个Vote表是一个明智的选择
            目前不用考虑给热搜事件列表排序的问题
  
    ```
* 修改其余所有接口：所有读取操作都改为从数据库中读取数据（包括重构测试）
  注意：需要修改热搜事件返回的数据结构，使其返回热搜事件id和获得的票数:
    ```
        {
            eventName: "event name",
            keyword: "keyword",
            id: "id",
            voteNum: 10
        }
    ```

* 写测试！！！
#### 重复课堂上的Demo完成练习
* 给所有接口添加错误处理：
    1. get /rs/list时对start和end进行校验，如果超出范围则返回 400 {error:"invalid request param"}
    2. get /rs/{index} 对index进行校验，如果超出范围则返回 400 {error:"invalid index"}
    3. post /rs/event 对RsEvent进行校验，如果不满足校验规则(User和RsEvent定义的校验规则)，则返回 400 {error:"invalid param"}
    4. post /user 对User进行校验，如果不满足校验规则，则返回 400 {error:"invalid user"}
    5. 先阅读：https://www.baeldung.com/spring-boot-logging
       在我们的exceptionHandler中添加日志，记录下错误的信息（error级别），运行程序试着观察是否有日志打印
* 先写测试（除了日志）！
注意：最终需要将改动合并到master分支
 #### 实现如下接口
 * 查询投票记录接口：
     参数传入起止时间，查询在该时间范围内的所有投票记录，写测试验证
 * 这是最后一堂JPA课，把前面所有没做完的作业包括课上demo的范例都补上！


