program A(a0);
    var a1 :integer;
    procedure B(b0 :integer);
        var b1 :integer;
        procedure C(c0 :integer);
            var c1 :integer;
        begin
            c1 := a1 + b1;
        end;
        procedure D(d0 :integer);
            var d1 :integer;
        begin
            d1 := a1 + b1;
            { d1 := a1 + b1 + c1; !!! c1は参照できない !!!}
            C(3)        {!!! Cは参照できる !!!}
        end;
    begin
    end;
begin
end.
    