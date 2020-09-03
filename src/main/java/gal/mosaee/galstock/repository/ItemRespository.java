package gal.mosaee.galstock.repository;

import gal.mosaee.galstock.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRespository extends JpaRepository<Item,Long> {

}
