import os
import sys
import persistence


def openconfig(config_arg):
    with open(config_arg, 'r') as config_file:
        confi= config_file.read()
    config=confi.splitlines()
    sums=config[0].split(",")

    persistence.psl.createTables()
    i=(int(sums[3]))+(int(sums[2]))+(int(sums[1]))+(int(sums[0]))
    while (i > (int(sums[2]))+(int(sums[1]))+(int(sums[0]))):
        logi= config[i].split(",")
        logi1 = persistence.LogisticDTO(logi[0], logi[1], logi[2], logi[3])
        persistence.psl.logisticsDAO.insert(logi1)
        i=i-1
    while (i >  +(int(sums[1])) + (int(sums[0]))):
        clin = config[i].split(",")
        clin1 = persistence.ClinicDTO(clin[0], clin[1], clin[2], clin[3])
        clinicsDAO1 = persistence.ClinicsDAO(persistence.psl.conn)
        clinicsDAO1.insert(clin1)
        i = i - 1
    while (i > +(int(sums[0]))):
        sup = config[i].split(",")
        sup1 = persistence.SupplierDTO(sup[0], sup[1], sup[2])
        suppliersDAO = persistence.SuppliersDAO(persistence.psl.conn)
        suppliersDAO.insert(sup1)
        i = i - 1
    while (i > 0):
        vac= config[i].split(",")
        vaccineDTO= persistence.VaccineDTO(vac[0], vac[1], vac[2], vac[3])
        vaccinesDAO = persistence.VacccinesDAO(persistence.psl.conn)
        vaccinesDAO.insert(vaccineDTO)
        i = i - 1

def main(argv):
    #creating the database from the config file
    openconfig(sys.argv[1])
    #updating the database acording to the orders
    with open(sys.argv[2], 'r') as orders_file:
        data = orders_file.readlines()

    #removes output file if exists
    output_file = sys.argv[3]
    #will we delete this?? :O
    if os.path.isfile(output_file):
        os.remove(output_file)
    #open a new output file
    outfile = open(output_file,"w")

    for line in data:
        line = line.rstrip('\n')
        counter = line.count(',')
        if counter == 2: #receive shipment order
            receiveShippment(line.split(','))
            output_line(outfile)
        if counter == 1:#send shipment order
            sendShipment(line.split(','))
            output_line(outfile)


def output_line(outfile):
    new_line = persistence.psl.output_new_line()
    outfile.write(f'{new_line[0]},{new_line[1]},{new_line[2]},{new_line[3]} \n')


def sendShipment(list):
    persistence.psl.sendShipment(list)


def receiveShippment(list):
     persistence.psl.receiveShipment(list)

if __name__ == '__main__':
    main(sys.argv)