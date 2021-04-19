package com.example.hotelix_application.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOut
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOutDatabaseDao
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.database.reservation.ReservationListDatabaseDao
import com.example.hotelix_application.database.Rooms.RoomsList
import com.example.hotelix_application.database.Rooms.RoomsListDatabaseDao
import com.example.hotelix_application.database.payment.Payment
import com.example.hotelix_application.database.payment.PaymentDao
import com.example.hotelix_application.database.status.Status
import com.example.hotelix_application.database.status.StatusDao

@Database(entities = [
    ReservationList::class,
    ClockInAndOut::class,
    Status::class,
    Payment::class,
    RoomsList::class
    //insert entities here
], version = 1, exportSchema = false)

@TypeConverters(Converters::class)
abstract class HotelixDatabase : RoomDatabase() {

    abstract val reservationListDao : ReservationListDatabaseDao
    abstract val clockDao : ClockInAndOutDatabaseDao
    abstract val statusDao: StatusDao
    abstract val paymentDao: PaymentDao
    abstract val roomsListDao :RoomsListDatabaseDao

    companion object{

        @Volatile
        private var INSTANCE: HotelixDatabase? = null

        fun getInstance(context: Context): HotelixDatabase{

            synchronized(this){

                var instance = INSTANCE

                //if instance is null make new database instance
                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            HotelixDatabase::class.java,
                            "hotelix_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}