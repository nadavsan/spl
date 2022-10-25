import os
import sqlite3
import atexit



class ClinicDTO:
    def __init__(self,id,location,demand,logistic):
        self.id=id
        self.location=location
        self.demand= demand
        self.logistic= logistic

class ClinicsDAO:
    def __init__(self, conn):
        self.conn=conn

    def insert(self, clinicDTO):
        self.conn.execute("""
            INSERT INTO clinics(id,location,demand,logistic) VALUES (?,?,?,?)
        """, [clinicDTO.id, clinicDTO.location, clinicDTO.demand, clinicDTO.logistic])

    def find(self, location):
        c = self.conn.cursor()
        c.execute("""
            SELECT id,location,demand,logistic FROM clinics WHERE location=?
        """, [location])
        clinic = c.fetchone()
        return ClinicDTO(*clinic)

    def total_demand(self):
        cursor = self.conn.cursor()
        cursor.execute("""SELECT demand FROM clinics""")
        sum = 0
        clinics_demand = cursor.fetchall()
        for tuple in clinics_demand:
            sum += tuple[0]
        return sum

    def updateAmount(self,location , demand):
        cursor = self.conn.cursor()
        cursor.execute(fr"""UPDATE clinics SET demand = {demand} WHERE location = '{location}'""")




class VaccineDTO:
    def __init__(self,id,date,supplier,quantity):
        self.id=id
        self.date= date
        self.supplier= supplier
        self.quantity =quantity


class VacccinesDAO:
    def __init__(self, conn):
        self.conn=conn

    def insert(self, VaccineDTO):
        self.conn.execute(fr"""
            INSERT INTO vaccines(date,supplier,quantity) VALUES ('{VaccineDTO.date}',{VaccineDTO.supplier}, {VaccineDTO.quantity})
        """)

    def get_first(self):
        c = self.conn.cursor()
        c.execute ("""
            SELECT id,date, supplier, quantity FROM vaccines
        """)
        vaccine = c.fetchone()
        return VaccineDTO(*vaccine)

    def total_inventory(self):
        cursor = self.conn.cursor()
        cursor.execute("""SELECT quantity FROM vaccines""")
        sum = 0
        vaccines = cursor.fetchall()
        for tuple in vaccines:
            sum += tuple[0]
        return sum

    def updateQuantity(self,quantity, id):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE vaccines SET quantity = ? WHERE id = ?
        """,[quantity,id])

    def remove(self,id):
        cursor = self.conn.cursor()
        cursor.execute("""
            DELETE FROM vaccines WHERE id = ?
        """, [id])




class SupplierDTO:
    def __init__(self, id, name, logistic_id):
        self.id = id
        self.name = name
        self.logistic_id = logistic_id


class SuppliersDAO:
    def __init__(self, conn):
        self.conn = conn

    def insert(self, supplier):
        self.conn.execute("""
                INSERT INTO suppliers (id,name,logistic) VALUES (?,?,?)
                """, [supplier.id, supplier.name, supplier.logistic_id])

    def find(self, id):
        c = self.conn.cursor()
        c.execute("""
                SELECT id,name ,logistic_id FROM suppliers WHERE id=?
                """, [id])
        return SupplierDTO(*c.fatchone())

    def getIdFromName(self, name):
        cursor = self.conn.cursor()
        cursor.execute(fr"""SELECT id FROM suppliers WHERE name = '{name}'""")
        return cursor.fetchone()[0]

    def getLogisticID(self,id):
        cursor = self.conn.cursor()
        cursor.execute("""
            SELECT logistic FROM suppliers WHERE id=?
        """, [id])
        return cursor.fetchone()[0]

class LogisticDTO:
    def __init__(self, id, name, count_sent, count_received):
        self.id = id
        self.name = name
        self.count_sent = count_sent
        self.count_received = count_received


class LogisticsDAO:
    def __init__(self, conn):
        self.conn = conn

    def insert(self, logistic):
        self.conn.execute("""
        INSERT INTO logistics (id,name,count_sent,count_received) VALUES (?,?,?,?)
        """, [logistic.id, logistic.name, logistic.count_sent, logistic.count_received])

    def find(self, id):
        cursor = self.conn.cursor()
        cursor.execute ("""
        SELECT id,name,count_sent,count_received FROM logistics WHERE id=?
        """, [id])
        return LogisticDTO(*cursor.fetchone())

    def total_sent_and_received(self):#returns a list of total sum sent (index 0) and total sum received (index 1)
        cursor = self.conn.cursor()
        cursor.execute("""SELECT count_sent, count_received FROM logistics""")
        sum_sent_received = [0, 0]
        sent_received = cursor.fetchall()
        for tuple in sent_received:
            sum_sent_received[0] += tuple[0]
            sum_sent_received[1] += tuple[1]
        return sum_sent_received


    def updateCount_sent(self,id,amount):
        cursor = self.conn.cursor()
        cursor.execute(""" UPDATE logistics SET count_sent = ? WHERE id = ? """, [amount, id])

    def updateCount_received(self,id,amount):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE logistics SET count_received = ? WHERE id = ?
        """, [amount,id])

class PersistenceLayer:

    def __init__(self):
        db_file = 'database.db'
        if os.path.isfile(db_file):
            os.remove(db_file)

        self.conn = sqlite3.connect('database.db')
        self.logisticsDAO = LogisticsDAO(self.conn)
        self.clinicsDAO = ClinicsDAO(self.conn)
        self.vaccinesDAO = VacccinesDAO(self.conn)
        self.suppliersDAO = SuppliersDAO(self.conn)

    def close(self):
        self.conn.commit()
        self.conn.close()

    def createTables(self):
        cursor = self.conn.cursor()
        cursor.execute("""create table logistics(id INTEGER PRIMARY KEY , name STRING NOT NULL , count_sent INTEGER NOT NULL , count_received INTEGER NOT NULL);""")
        cursor.execute("""create table suppliers(id INTEGER PRIMARY KEY , name STRING NOT NULL , logistic INTEGER REFERENCES Logistic(id));""")
        cursor.execute("""create table clinics(id INTEGER PRIMARY KEY ,location STRING NOT NULL ,demand INTEGER NOT NULL , logistic INTEGER REFERENCES Logistic(id));""")
        cursor.execute("""CREATE TABLE vaccines(id INTEGER PRIMARY KEY ,date DATE NOT NULL , supplier INTEGER REFERENCES Supplier(id), quantity INTEGER NOT NULL);""")

    def receiveShipment(self,list):
        #making variables out of the list
        name = list[0]
        amount = int(list[1])
        date = list[2]
        #finding the supplier's id
        suppliersDAO = SuppliersDAO(self.conn)
        supID = suppliersDAO.getIdFromName(name)
        dummy_id = 1000  #toFix
        #adding a new shipment to the vaccines table
        vaccineDTO = VaccineDTO(dummy_id, date, supID, amount)
        vaccines = VacccinesDAO(self.conn)
        vaccines.insert(vaccineDTO)
        #updating count_received in logistics table
        logisticID = suppliersDAO.getLogisticID(supID)
        logisticDAO = LogisticsDAO(self.conn)
        logisticDTO = logisticDAO.find(logisticID)
        count_received = logisticDTO.count_received + amount
        logisticDAO.updateCount_received(logisticID,count_received)

    def sendShipment(self, shipment):
        #making variables out of the list
        location = shipment[0]
        amount = int(shipment[1])

        #updating demand
        clinicsDAO = self.clinicsDAO
        clinicDTO = clinicsDAO.find(location)
        new_demand = clinicDTO.demand - amount
        clinicsDAO.updateAmount(location,new_demand)

        #updating count_sent
        logisticsDAO = self.logisticsDAO
        logisticData = logisticsDAO.find(clinicDTO.logistic)
        count_sent = logisticData.count_sent
        count_sent += amount
        logisticsDAO.updateCount_sent(logisticData.id,count_sent)

        #updating vaccines table and removing rows if necessary
        vaccinesDAO = VacccinesDAO(self.conn)
        vaccineDTO = vaccinesDAO.get_first()
        quantity = vaccineDTO.quantity
        while(amount != 0):
            if quantity < amount:#decreases amount (toShip) by quantity of the first vaccine and gets quantity of next vaccine
                amount = amount - quantity
                vaccinesDAO.remove(vaccineDTO.id)
                vaccineDTO = vaccinesDAO.get_first()
                quantity = vaccineDTO.quantity
            elif quantity > amount:#decreases and updates
                quantity = quantity - amount
                vaccinesDAO.updateQuantity(quantity, vaccineDTO.id)
                amount = 0
            else:
                vaccinesDAO.remove(vaccineDTO.id)
                amount = 0

    def output_new_line(self):
        inventory = self.vaccinesDAO.total_inventory()
        demand = self.clinicsDAO.total_demand()
        sent_received = self.logisticsDAO.total_sent_and_received()
        sent = sent_received[0]
        received = sent_received[1]
        output_line = [inventory, demand, received, sent]
        return output_line





#persistence layer is a singleton
psl = PersistenceLayer()

atexit.register(psl.close)
