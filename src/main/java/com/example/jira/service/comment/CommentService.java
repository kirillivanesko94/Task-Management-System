package com.example.jira.service.comment;

import com.example.jira.db.comment.Comment;
import com.example.jira.db.comment.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(CommentDto commentDto, UUID authorId, UUID taskId) {
        Comment comment = Comment.createNewComment(commentDto.getBody(), authorId, taskId);

        commentRepository.insert(comment);

        return comment;
    }

    public Comment getCommentById(UUID commentId) {
        return commentRepository.getById(commentId);
    }
    
    public void deleteCommentById(UUID commentID) {
        commentRepository.deleteById(commentID);
    }
    
   public void updateComment(CommentDto dto, UUID commentId) {
        Comment foundComment = commentRepository.getById(commentId);
        foundComment.setBody(dto.getBody());

        commentRepository.update(foundComment);
   }
}
