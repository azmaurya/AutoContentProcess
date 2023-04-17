package com.hungama.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hungama.model.PosGreyUserModel;


@Repository
public interface PosgreyRepository extends JpaRepository<PosGreyUserModel, Long>
{
	@Query(value ="select c.content_id,c.vendor_id ,v.vendor_name,cs.added_by ,c.content_code from mvcms.tbl_contents c inner join mvcms.tbl_content_status cs on c.content_id=cs.content_id inner join mvcms.tbl_vendors v on c.vendor_id=v.vendor_id where c.content_id in(:content_id)", nativeQuery = true)
	ArrayList<String> findByContentCode(long content_id);
	
	
	@Query(value ="select package_content_id,content_id from mvcms.TBL_Package_content_map where package_content_id in(:content_id)", nativeQuery = true)
	ArrayList<String> getAlbumTrackContentId(long content_id);

	
	@Query(value ="select content_id from mvcms.TBL_contents where original_content_code=:content_id", nativeQuery = true)
	int getAlbumTrackIdWithUPC(String content_id);
	
	
	
	@Query(value ="select * from mvcms.tbl_package_content_map where content_id in(:content_id)", nativeQuery = true)
	String getTrackAlbumContentId(int content_id);
	
	@Query(value ="select count(*) cnt from mvcms.tbl_sqs_requests where content_id in (:content_id)", nativeQuery = true)
	int TranscodingSqsRequestCheck(Set<Integer> content_id);

	@Query(value ="select content_id,retailer_id,rights_status from mvcms.tbl_content_rights_status where content_id in (:content_id)", nativeQuery = true)
	List<String> RightsStatusCheck(Set<Integer> content_id);



}
