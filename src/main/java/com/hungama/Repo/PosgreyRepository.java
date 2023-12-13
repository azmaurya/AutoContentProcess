package com.hungama.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.hungama.model.PosGreyUserModel;


@Repository
public interface PosgreyRepository extends JpaRepository<PosGreyUserModel, Long>
{
	@Query(value ="select c.content_id,c.vendor_id ,v.vendor_name,cs.added_by ,c.content_code from mvcms.tbl_contents c inner join mvcms.tbl_content_status cs on c.content_id=cs.content_id inner join mvcms.tbl_vendors v on c.vendor_id=v.vendor_id where c.content_id in(:content_id)", nativeQuery = true)
	ArrayList<String> findByContentCode(long content_id);
	
	
	@Query(value ="select package_content_id,content_id from mvcms.TBL_Package_content_map where package_content_id in(:content_id)", nativeQuery = true)
	ArrayList<String> getAlbumTrackContentId(long content_id);
	
	@Query(value ="select package_content_id,content_id from mvcms.TBL_Package_content_map where package_content_id in(:content_id)", nativeQuery = true)
	ArrayList<String> getAlbumTrackContentIdFortseries(List<Integer> content_id);
	
	@Query(value ="select package_content_id,content_id from mvcms.TBL_Package_content_map where package_content_id in(:content_id)", nativeQuery = true)
    ArrayList<String> getAlbumTrackContentIdbatch(List<Integer> content_id);
	
	@Query(value ="select package_content_id,content_id from mvcms.TBL_Package_content_map where package_content_id in(:content_id)", nativeQuery = true)
	List<Integer> getAlbumTrackContentIdbatch(Long content_id);
	
	
	@Query(value ="select package_content_id,content_id from mvcms.tbl_package_content_map where content_id in(:content_id)", nativeQuery = true)
	ArrayList<String> getTrackAlbumContentId(long content_id);

	@Query(value ="select content_id from mvcms.TBL_contents where original_content_code=:content_id", nativeQuery = true)
	Integer getAlbumTrackIdWithUPC(String content_id);
	
	@Query(value ="select content_id from mvcms.TBL_contents where original_content_code=:content_id", nativeQuery = true)
	String  getAlbumTrackIdWithbatch(String content_id);
	
	
	@Query(value ="select count(*) cnt from mvcms.tbl_sqs_requests s inner join mvcms.tbl_content_files cf on s.raw_file_id = cf.file_id\n"
			+ "where s.content_id in (:content_id)", nativeQuery = true)
	int TranscodingSqsRequestCheck(Set<Integer> content_id);
	
	
	@Query(value ="select count(*) cnt from mvcms.tbl_sqs_requests s inner join mvcms.tbl_content_files cf on s.raw_file_id = cf.file_id\n"
			+ "where s.content_id in (:content_id)", nativeQuery = true)
	int TranscodingSqsRequestBulkCheck(List<Integer> content_id);

	@Query(value ="select content_id,rights_status,retailer_id from mvcms.tbl_content_rights_status where content_id in (:content_id) and retailer_id in(1242) ", nativeQuery = true)
	List<String[]> RightsStatusCheck(Set<Integer> content_id);


	@Query(value ="select distinct s.content_id,s.status from mvcms.tbl_sqs_requests s inner join mvcms.tbl_content_files cf on s.raw_file_id = cf.file_id where s.content_id in (:content_id) order by s.status desc ", nativeQuery = true)
	ArrayList<String> requestStatus(List<Integer> content_id);
	
	@Query(value ="select distinct s.content_id,s.status from mvcms.tbl_sqs_requests s inner join mvcms.tbl_content_files cf on s.raw_file_id = cf.file_id where s.content_id in (:content_id) order by s.status desc ", nativeQuery = true)
	List<String[]> requestStatuspending(List<Integer> content_id);
	
	//@Query(value ="select s.content_id,s.content_file_type_id,s.status from mvcms.tbl_sqs_requests s inner join mvcms.tbl_content_files cf on s.raw_file_id = cf.file_id where s.content_id in (:content_id)", nativeQuery = true)
	//ArrayList<String> requestStatusnum(long content_id);
	
	
	@Modifying
	@Transactional
	@Query(value ="update mvcms.tbl_sqs_requests set status='QUEUED', priority=14,request_sent_on = current_timestamp where content_id in (:content_id) and request_xml is not null", nativeQuery = true)
	int updatecount_faild(Long content_id);
	
	@Modifying
	@Transactional
	@Query(value ="update mvcms.tbl_sqs_requests set status='QUEUED', priority=14,request_sent_on = current_timestamp where content_id in (:content_id) and request_xml is not null", nativeQuery = true)
	int Bulkupdatecount_faild(List<Integer> content_id);
	
	@Modifying
	@Transactional
	@Query(value ="update mvcms.tbl_sqs_requests set  priority = 15,  request_sent_on = current_timestamp where content_id in (:content_id) and status='PENDING'", nativeQuery = true)
	int updatecount_pending(Long content_id);
}
