import random
import math
import xlsxwriter
from xlsxwriter.utility import xl_rowcol_to_cell
from tqdm import tqdm
import time
import pathlib
from pathlib import Path
import os

#defines the number of years to simulate 
n_anos = 10

#find current directory where the script is running and appends to it the file name to create
file_name = Path("Caso_3_resultados.xlsx")
path = os.path.join(pathlib.Path(__file__).parent.absolute(), file_name)

rand_vec = []

def init_rands():
    for i in range(365*n_anos):
        rand_vec.append(random.random())

#simulation function for 365*n_anos days with 'p' days between maintenance
def simulation(p):
    dias_sem_m = 0
    prob_avaria = 0
    cont_avarias = 0
    cont_manutencao_prev = 0

    init_rands()

    for i in range(len(rand_vec)):

        if prob_avaria > random.random():
            cont_avarias = cont_avarias + 1
            dias_sem_m = 0
        elif dias_sem_m >= p:
            cont_manutencao_prev = cont_manutencao_prev + 1
            dias_sem_m = 0
        else:
            dias_sem_m = dias_sem_m + 1

        prob_avaria = 1 - math.exp(-dias_sem_m/10000)

    rand_vec.clear()
    #print('cont avarias: ', cont_avarias, 'cont manutenção: ', cont_manutencao_prev)
    #cost return
    return ((cont_avarias*5000) + (cont_manutencao_prev*500))


#main function for the whole simulation
def main():

    #ask to input the number of simulation to perform
    print('Introduzir o número de simulações a fazer:')
    n_simulations = int(input())

    # Create a workbook and add both worlsheets.
    workbook = xlsxwriter.Workbook(path)
    grafico = workbook.add_worksheet("média_e_gráficos")
    dados = workbook.add_worksheet("dados_simulações")
    
    dados.write(0, 0, "Período entre manutenções preventivas (dias)")


    start_time = time.time()

    for col in tqdm(range(n_simulations)):

        dados.write(0, col+1, "Custos (" + str(col+1) + ")")

        for row in range (365):

            #write the values obtained in the excel file
            dados.write(row+1, 0, row)
            grafico.write(row+1, 0, row)
            dados.write(row+1, col+1, simulation(row)/n_anos)

    #create the chart
    grafico.write(0, 0, "Período entre manutenções preventivas (dias)")
    grafico.write(0, 1, "Média do custo (€)")

    j=1

    #calculate average 
    while j <= 365:

        inicio = xl_rowcol_to_cell(j, 1)
        fim = xl_rowcol_to_cell(j, n_simulations)

        grafico.write_formula(j, 1,"=Average(dados_simulações!" + inicio + ":" + fim + ")")

        j = j + 1

    #create the chart
    chart = workbook.add_chart({'type': 'scatter'})

    chart.add_series({'categories':'=média_e_gráficos!$A$2:$A$366', 
                        'values':'=média_e_gráficos!$B$2:$B$366',
                        'name': 'Média de Custos (€)',
                        'marker':{'type':'circle'}})

                        # Configure the chart axes.
    chart.set_x_axis({'name': 'Período entre manutenções preventivas (dias)'})
    chart.set_y_axis({'name': 'Média do custo total (€)'})

    grafico.insert_chart('G3', chart)

    grafico.write("D4", "simulações feitas")
    grafico.write("D5", n_simulations)

    grafico.write("D1", "tempo decorrido")
    grafico.write("D2", (time.time() - start_time)/60)
    grafico.write("E2", "minutos")

    #saves the excel file
    workbook.close()


main()
