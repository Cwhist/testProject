package kr.ac.hansung.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ac.hansung.dao.ReplyDao;
import kr.ac.hansung.model.Reply;

@Service
public class ReplyService {
	
	@Autowired
	private ReplyDao replyDao;
	
	public List<Reply> getCurrent(int postNo) {
		List<Reply> list = replyDao.getReplies(postNo);
		for(Reply reply : list) {
			if(reply.getParentId() != 0) {
				updateParentAuthor(reply);
			}
		}
		return list;
	}
	
	public List<Reply> getBestReplies(int postNo) {
		List<Reply> list = replyDao.getBestReplies(postNo);
		for(Reply reply : list) {
			if(reply.getParentId() != 0) {
				updateParentAuthor(reply);
			}
		}
		return list;
	}
	
	public Reply getReply(int replyId) {
		return replyDao.getReply(replyId);
	}

	public void insert(Reply reply) {
		replyDao.insert(reply);
	}
	
	public void delete(Reply reply) {
		replyDao.delete(reply);
	}
	
	public void refreshBestReplies(int postNo) {
		List<Reply> descList = replyDao.getLikeDescReplies(postNo);
		for(int i=0; i<descList.size(); i++) {
			if(i<3) {
				Reply reply = descList.get(i);
				if(reply.getLikeCount() >= 10)
					reply.setBest(true);
				replyDao.update(reply);
			} else {
				Reply reply = descList.get(i);
				if(reply.isBest()) {
					reply.setBest(false);
					replyDao.update(reply);
				}
			}
		}
	}
	
	public void like(Reply reply) {
		replyDao.like(reply);
		refreshBestReplies(reply.getPostNo());
	}
	
	public void unlike(Reply reply) {
		replyDao.unlike(reply);
	}
	
	public void updateParentAuthor(Reply reply) {
		reply.setParentAuthor(replyDao.getReply(reply.getParentId()).getAuthor());
	}
	
	public int getNextOrderNo(int postNo) {
		return replyDao.getCurrentOrderNo(postNo)+1;
	}
	
	public int getNextOrderNo(int postNo, int groupNo) {
		return replyDao.getCurrentOrderNo(postNo, groupNo)+1;
	}
	
	public int getNextGroupNo(int postNo) {
		return replyDao.getCurrentGroupNo(postNo)+1;
	}

}