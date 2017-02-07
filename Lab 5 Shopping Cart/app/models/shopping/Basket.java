package models.shopping;

import com.avaje.ebean.Model;
import models.products.Product;
import models.users.Customer;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;


// Product entity managed by Ebean
@Entity
public class Basket extends Model {

    @Id
    private Long id;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.PERSIST)
    private List<OrderItem> basketItems;
    
    @OneToOne
    private Customer customer;

    // Default constructor
    public  Basket() {
    }
    
    public void addProduct(Product p){

        boolean itemFound = false;
        //Check if product exists in basket
        for (OrderItem i : basketItems){
            if (i.getProduct().getId() == p.getId()){
                i.increaseQty();
                itemFound = true;
                break;
            }
        }
        if (itemFound == false){
            //Add orderItem to list
            OrderItem newItem = new OrderItem(p);
            //Add to items
            basketItems.add(newItem);
        }
    }

    public void removeItem(OrderItem item){
        //use iterator for safety
        //removal of list items is unreliable
        //iterator works with object ref.
        for (Iterator<OrderItem> iter = basketItems.iterator(); iter.hasNext();){
            OrderItem i = iter.next();
            if (i.getId().equals(item.getId())){
                //more than one item than decrement
                if (i.getQuantity() > 1){
                    i.decreaseQty();
                }
                //if only one left remove item from basket via iterator
                else{
                    //del object from db
                    i.delete();
                    //remove object from list
                    iter.remove();
                    break;
                }
            }
        }
    }

    public void removeAllItems() {
        for(OrderItem i: this.basketItems) {
            i.delete();
        }
        this.basketItems = null;
    }

    public double getBasketTotal() {
        
        double total = 0;
        
        for (OrderItem i: basketItems) {
            total += i.getItemTotal();
        }
        return total;
    }
	
	//Generic query helper
    public static Finder<Long,Basket> find = new Finder<Long,Basket>(Basket.class);

    //Find all Products in the database
    public static List<Basket> findAll() {
        return Basket.find.all();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getBasketItems() {
        return basketItems;
    }

    public void setBasketItems(List<OrderItem> basketItems) {
        this.basketItems = basketItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

