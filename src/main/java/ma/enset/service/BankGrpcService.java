package ma.enset.service;

import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.bankServiceGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcService extends bankServiceGrpc.bankServiceImplBase {

    @Override
    public void convert(Bank.convertCurrencyRequest request, StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
         String form = request.getFrom();
         String to = request.getTo();
         double amount = request.getAmount();

         Bank.convertCurrencyResponse response = Bank.convertCurrencyResponse.newBuilder()
                 .setAmount(amount)
                 .setFrom(form)
                 .setTo(to)
                 .setResult(amount * 11.4)
                 .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
    }

    @Override
    public void getCurrencyStream(Bank.convertCurrencyRequest request, StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
        String form = request.getFrom();
        String to = request.getTo();
        double amount = request.getAmount();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = 0;
                           @Override
                           public void run() {
                                 Bank.convertCurrencyResponse response = Bank.convertCurrencyResponse.newBuilder()
                                        .setAmount(amount)
                                        .setFrom(form)
                                        .setTo(to)
                                        .setResult(amount * Math.random() * 100)
                                        .build();
                                 responseObserver.onNext(response);
                                    count++;
                                    if(count == 10) {
                                        responseObserver.onCompleted();
                                        timer.cancel();
                                    }
                           }
                       }, 1000, 1000);

    }



    @Override
    public StreamObserver<Bank.convertCurrencyRequest> performSteam(StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
        return super.performSteam(responseObserver);
    }

    @Override
    public StreamObserver<Bank.convertCurrencyRequest> fullStream(StreamObserver<Bank.convertCurrencyResponse> responseObserver) {
        return super.fullStream(responseObserver);
    }
}
