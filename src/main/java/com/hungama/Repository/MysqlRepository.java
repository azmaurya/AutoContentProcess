package com.hungama.Repository;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hungama.UserModel.MysqlUserModel;

@Repository
public interface MysqlRepository extends JpaRepository<MysqlUserModel, Long>
{
	@Query(value="select urlPath from tseries.TBL_TSERIES_S3_UPLOAD  WHERE urlPath like lower(concat('%', concat(:contentCode, '%.xml')))" , nativeQuery = true)
	List<String> getUPCFromBatchId(String contentCode);
	
	@Query(value="select id,contentCode,path,type,subType from orchard.TBL_ORCHARD_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getOrchardFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from orchard.TBL_ORCHARD_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getOrchardFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from tseries.TBL_TSERIES_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getTSeriseFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from tseries.TBL_TSERIES_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getTSeriseFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from emi.TBL_EMI_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getUniversalFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from emi.TBL_EMI_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getUniversalFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from fuga.TBL_FUGA_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getFugaFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from fuga.TBL_FUGA_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getFugaFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from BelieveDigitalSAS.TBL_BELIEVE_DIGITAL_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getBeliveFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from BelieveDigitalSAS.TBL_BELIEVE_DIGITAL_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getBeliveFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from saregama.TBL_SAREGAMA_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getSaregamaFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from saregama.TBL_SAREGAMA_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getSaregamaFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from wbclient.TBL_WARNER_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getWarnerFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from wbclient.TBL_WARNER_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getWarnerFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from divo.TBL_DIVO_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> getDivoFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from divo.TBL_DIVO_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getDivoFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from erik.TBL_ERIK_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))"  , nativeQuery = true)
	List<String> geterikFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from erik.TBL_ERIK_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int geterikFileDelete(long fileId);
		
	@Query(value="select id,contentCode,path,type,subType from pdlindiahun.TBL_PDLINDIA_HUN_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getPhonographicFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from pdlindiahun.TBL_PDLINDIA_HUN_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getPhonographicFileDelete(long fileId);
	
	@Query(value="select id,contentCode,path,type,subType from dpmnetworks.TBL_DPMNETWORK_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getDPMFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from dpmnetworks.TBL_DPMNETWORK_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getDPMFileDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from zeel.TBL_ZEEL_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getzeelFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from zeel.TBL_ZEEL_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getzeelFileDelete(long fileId);
	
	@Query(value="select id,contentCode,path,type,subType from onedigital.TBL_ONEDIGITAL_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getonedigitalFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from onedigital.TBL_ONEDIGITAL_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getonedigitalDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from sonydadc.TBL_SONY_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getSonyFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from sonydadc.TBL_SONY_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getsonyDelete(long fileId);
	
	
	@Query(value="select id,contentCode,path,type,subType from cdbaby.TBL_CDBABY_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getcdbabyFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from cdbaby.TBL_CDBABY_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getcdbayDelete(long fileId);
	
	@Query(value="select id,contentCode,path,type,subType from ingrooves.TBL_INGROOVES_S3_MOVEFILE where status='Y' and lower(contentCode) like lower(concat('%', concat(:contentCode, '%')))" , nativeQuery = true)
	List<String> getINgroovesFileMapping(String contentCode);
	@Transactional
	@Modifying
	@Query(value="delete from ingrooves.TBL_INGROOVES_S3_MOVEFILE where id=:fileId", nativeQuery = true)
	int getINgroovesDelete(long fileId);
	
	
	
	
	
	
	
	
	
	
	
	
	
}
