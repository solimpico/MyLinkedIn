package it.unisalento.mylinkedin.iservices;

import it.unisalento.mylinkedin.domain.Comment;
import it.unisalento.mylinkedin.exceptions.CommentNotFoundException;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;

import java.util.List;

public interface ICommentService {
    Comment save(Comment comment) throws PostNotFoundException;
    Comment findById(int id) throws CommentNotFoundException;
    void deleteById(int id) throws CommentNotFoundException;
}
