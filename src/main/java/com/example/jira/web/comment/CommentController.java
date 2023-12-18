package com.example.jira.web.comment;

import com.example.jira.db.comment.Comment;
import com.example.jira.scurity.SecurityUtils;
import com.example.jira.service.comment.CommentDto;
import com.example.jira.service.comment.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping
    Comment createComment(@RequestBody CommentDto commentDto, @RequestParam UUID taskID) {
        return commentService.createComment(commentDto, SecurityUtils.getCurrentUserId(), taskID);
    }
    @GetMapping("{id}")
    Comment getCommentByID(@PathVariable UUID commentId) {
        return commentService.getCommentById(commentId);
    }
    @DeleteMapping("{id}")
    void deleteCommentById(@PathVariable UUID commentId) {
         commentService.deleteCommentById(commentId);
    }
    @PutMapping("{id}")
    void updateComment(@PathVariable UUID commentId, @RequestBody CommentDto dto) {
        commentService.updateComment(dto, commentId);
    }

}
