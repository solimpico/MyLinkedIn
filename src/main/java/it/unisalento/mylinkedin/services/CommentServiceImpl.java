package it.unisalento.mylinkedin.services;

import ch.qos.logback.core.encoder.EchoEncoder;
import it.unisalento.mylinkedin.dao.CommentRepository;
import it.unisalento.mylinkedin.domain.Comment;
import it.unisalento.mylinkedin.exceptions.CommentNotFoundException;
import it.unisalento.mylinkedin.exceptions.PostNotFoundException;
import it.unisalento.mylinkedin.iservices.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    @Transactional(rollbackOn = PostNotFoundException.class)
    public Comment save(Comment comment) throws PostNotFoundException{
        try {
            return commentRepository.save(comment);
        }
        catch (Exception e){
            throw new PostNotFoundException();
        }
    }

    @Override
    @Transactional(rollbackOn = CommentNotFoundException.class)
    public Comment findById(int id) throws CommentNotFoundException{return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException());
    }

    @Override
    @Transactional(rollbackOn = CommentNotFoundException.class)
    public void deleteById(int id) throws CommentNotFoundException{
        try{
            commentRepository.deleteById(id);
        }
        catch (Exception e){
            throw new CommentNotFoundException();
        }
    }
}
