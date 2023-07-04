package com.scheduler.sgbdtrab2;
public class WaitDie {
    public String execute(Integer ti, Integer tj, TrManager trManager, LockTable lockTable){
        if(ti < tj ){

            trManager.setStatus(ti, "esperando");
            return "esperando";
        }else{
            trManager.setStatus(ti, "abortada");
            //remover da locktable os bloqueios
            lockTable.commit(ti);
            return "abortada";
        }
    }
}
