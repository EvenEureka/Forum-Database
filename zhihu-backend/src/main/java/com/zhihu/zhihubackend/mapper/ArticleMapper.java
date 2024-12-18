package com.zhihu.zhihubackend.mapper;

import com.zhihu.zhihubackend.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //新增
    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time) VALUES " +
            "(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    // 条件分页查询(动态sql)
    List<Article> list(@Param("userId") Integer userId, @Param("categoryId") Integer categoryId, @Param("state") String state);

    // 根据id查询
    @Select("select * from article where id =#{id}")
    Article findById(Integer id);

    //更新文章
    @Update("update article set title=#{title},content=#{content}," +
            "cover_img=#{coverImg},state=#{state},category_id=#{categoryId}," +
            "create_user=#{createUser},update_time=#{updateTime} where " +
            "id = #{id}")
    void update(Article article);

    // 删除文章
    @Delete("delete from article where id = #{id}")
    void delete(Integer id);
}
